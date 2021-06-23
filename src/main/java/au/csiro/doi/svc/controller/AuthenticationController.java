package au.csiro.doi.svc.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.csiro.doi.svc.db.RoleRepository;
import au.csiro.doi.svc.db.UserRepository;
import au.csiro.doi.svc.entity.Role;
import au.csiro.doi.svc.entity.RoleType;
import au.csiro.doi.svc.entity.Users;
import au.csiro.doi.svc.exception.RestApiException;
import au.csiro.doi.svc.login.LoginRequest;
import au.csiro.doi.svc.login.MessageResponse;
import au.csiro.doi.svc.login.SignUpRequest;
import au.csiro.doi.svc.login.TokenResponse;
import au.csiro.doi.svc.security.JwtUtils;
import au.csiro.doi.svc.services.RESTUser;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * This API has been created to work with JWT Authentication.
 * To enable it uncomment the relevant code in {@link WebSecurityConfig#configure(HttpSecurity http)}
 * It will then be available via Postman
 * 
 * @author pag06d
 *
 */
@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends au.csiro.doi.svc.controller.RestController
{

    /**
     * the authenticationManager
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * the userRepository
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * the roleRepository
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * the encoder
     */
    @Autowired
    private PasswordEncoder encoder;

    /**
     * the jwtUtils
     */
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 
     * @param loginRequest loginRequest
     * @return authentication response
     */
    //@ApiOperation(hidden=true, value = "Authenticate user")
    //@PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        RESTUser userDetails = (RESTUser) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new TokenResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    
    /**
     * 
     * @param signUpRequest signUpRequest
     * @param request HttpServletRequest
     * @return register response
     * 
     */
    @ApiOperation(hidden=true, value = "Sign up user")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request)
    {
    	//Authenticate rest user
    	RESTUser restUser = null;    	
    	try
    	{
    		restUser = validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.UNAUTHORIZED);    		
    	}   
    	
    	//Authorize admin user    	
    	if (!isAdminUser(restUser.getUsername()))
    	{
    		return new ResponseEntity<String>("Authorization is required to access this web service.", 
    				HttpStatus.UNAUTHORIZED);
    	}
    	 
    	
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

		if (!StringUtils.isEmpty(signUpRequest.getEmail()) && userRepository.existsByEmail(signUpRequest.getEmail())) 
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));			
		}

        // Create new user's account
        Users user = new Users(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (CollectionUtils.isEmpty(strRoles))
        {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER.toString())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
        else
        {
            strRoles.forEach(role -> {
                switch (role)
                {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN.toString())
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case "mod":
                    Role modRole = roleRepository.findByName(RoleType.ROLE_MODERATOR.toString())
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleType.ROLE_USER.toString())
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        Users savedUser = userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    
    private boolean isAdminUser(String restUser)
    {
    	boolean authorized = false;
        Users authUser = userRepository.findByUsername(restUser)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + restUser));
    	
    	Set<Role> userRoles = authUser.getRoles();
    	for (Role role : userRoles)
    	{
    		if (role.getName().equals(RoleType.ROLE_ADMIN.toString()))
    		{
    			authorized = true;
    			break;   			
    		}
    	}
    	return authorized;
    	
    }


	public AuthenticationManager getAuthenticationManager() 
	{
		return authenticationManager;
	}


	public void setAuthenticationManager(AuthenticationManager authenticationManager) 
	{
		this.authenticationManager = authenticationManager;
	}


	public UserRepository getUserRepository() 
	{
		return userRepository;
	}


	public void setUserRepository(UserRepository userRepository) 
	{
		this.userRepository = userRepository;
	}


	public RoleRepository getRoleRepository() 
	{
		return roleRepository;
	}


	public void setRoleRepository(RoleRepository roleRepository) 
	{
		this.roleRepository = roleRepository;
	}


	public PasswordEncoder getEncoder() 
	{
		return encoder;
	}


	public void setEncoder(PasswordEncoder encoder) 
	{
		this.encoder = encoder;
	}


	public JwtUtils getJwtUtils() 
	{
		return jwtUtils;
	}


	public void setJwtUtils(JwtUtils jwtUtils) 
	{
		this.jwtUtils = jwtUtils;
	}
    
}
