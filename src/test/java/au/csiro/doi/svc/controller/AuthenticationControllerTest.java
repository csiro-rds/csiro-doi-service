package au.csiro.doi.svc.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import au.csiro.doi.svc.db.RoleRepository;
import au.csiro.doi.svc.db.UserRepository;
import au.csiro.doi.svc.entity.Role;
import au.csiro.doi.svc.entity.RoleType;
import au.csiro.doi.svc.entity.Users;
import au.csiro.doi.svc.login.SignUpRequest;
import au.csiro.doi.svc.services.RESTUser;
import au.csiro.doi.svc.services.RESTUserService;

/**
 * @author Xiangtan Lin
 *
 */
public class AuthenticationControllerTest 
{
    @InjectMocks
    private AuthenticationController controller;
    
    @Mock
    private RESTUserService restUserService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private PasswordEncoder encoder;
    
    @Mock
    private HttpServletRequest mockedRequest;
    
    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
		mockedRequest = Mockito.mock(HttpServletRequest.class); 
		
    }
    
    @Test
    public void testAdminRegisterUserSuccess()
    {
    	Users adminUser = createAmdinUser();
		Optional<Users> aUser = Optional.of(adminUser);
		RESTUser user = new RESTUser(adminUser);		
		user.setUsername("user");
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);       
        when(userRepository.findByUsername(user.getUsername())).thenReturn(aUser);        
    	
    	SignUpRequest signUpRequest = new SignUpRequest();
    	signUpRequest.setUsername("userName");
    	signUpRequest.setPassword("password");
    	
    	Users newUser = new Users(signUpRequest.getUsername(), signUpRequest.getEmail(), "encoded_password");
    	
    	Role userRole = new Role (RoleType.ROLE_USER.toString());
    	Optional<Role> uRole = Optional.of(userRole);
    	when(encoder.encode(signUpRequest.getPassword())).thenReturn("encoded_password");
    	when(roleRepository.findByName(RoleType.ROLE_USER.toString())).thenReturn(uRole);
    	when(userRepository.save(newUser)).thenReturn(newUser);
    	ResponseEntity<?> result = controller.registerUser(signUpRequest, mockedRequest);
    	assertEquals(result.getStatusCode(), HttpStatus.OK);    	
    } 
    
    @Test
    public void testAdminRegisterAdminUserSuccess()
    {
    	Users adminUser = createAmdinUser();
		Optional<Users> aUser = Optional.of(adminUser);
		RESTUser user = new RESTUser(adminUser);		
		user.setUsername("user");
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);       
        when(userRepository.findByUsername(user.getUsername())).thenReturn(aUser);        
    	
    	SignUpRequest signUpRequest = new SignUpRequest();
    	signUpRequest.setUsername("userName");
    	signUpRequest.setPassword("password");
    	signUpRequest.getRoles().add("admin");
    	signUpRequest.getRoles().add("mod");
    	signUpRequest.getRoles().add("user");    	
    	
    	Users newUser = new Users(signUpRequest.getUsername(), signUpRequest.getEmail(), "encoded_password");
    	
    	Role adminRole = new Role (RoleType.ROLE_ADMIN.toString());
    	Optional<Role> aRole = Optional.of(adminRole);
    	Role modRole = new Role (RoleType.ROLE_MODERATOR.toString());
    	Optional<Role> mRole = Optional.of(modRole);
    	Role userRole = new Role (RoleType.ROLE_USER.toString());
    	Optional<Role> uRole = Optional.of(userRole);  	
    	
    	when(encoder.encode(signUpRequest.getPassword())).thenReturn("encoded_password");
    	when(roleRepository.findByName(RoleType.ROLE_ADMIN.toString())).thenReturn(aRole);
    	when(roleRepository.findByName(RoleType.ROLE_MODERATOR.toString())).thenReturn(mRole);
    	when(roleRepository.findByName(RoleType.ROLE_USER.toString())).thenReturn(uRole);
    	when(userRepository.save(newUser)).thenReturn(newUser);
    	ResponseEntity<?> result = controller.registerUser(signUpRequest, mockedRequest);
    	assertEquals(result.getStatusCode(), HttpStatus.OK);    	
    } 
    
    @Test
    public void testAdminRegisterUserSameUserNameFailure()
    {
    	Users adminUser = createAmdinUser();
		Optional<Users> aUser = Optional.of(adminUser);
		RESTUser user = new RESTUser(adminUser);		
		user.setUsername("user");
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);       
        when(userRepository.findByUsername(user.getUsername())).thenReturn(aUser);        
    	
    	SignUpRequest signUpRequest = new SignUpRequest();
    	signUpRequest.setUsername("userName");
    	signUpRequest.setPassword("password");
    	
    	when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);
    	
    	ResponseEntity<?> result = controller.registerUser(signUpRequest, mockedRequest);
    	assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);    	
    } 
    
    @Test
    public void testAdminRegisterUserSameEmailFailure()
    {
    	Users adminUser = createAmdinUser();
		Optional<Users> aUser = Optional.of(adminUser);
		RESTUser user = new RESTUser(adminUser);		
		user.setUsername("user");
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);       
        when(userRepository.findByUsername(user.getUsername())).thenReturn(aUser);        
    	
    	SignUpRequest signUpRequest = new SignUpRequest();
    	signUpRequest.setUsername("userName");
    	signUpRequest.setPassword("password");
    	signUpRequest.setEmail("email");
    	
    	when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);
    	
    	ResponseEntity<?> result = controller.registerUser(signUpRequest, mockedRequest);
    	assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);    	
    }
    
    @Test
    public void testNonAdminRegisterUserFailure()
    {
    	Users uUser = createUser();
		Optional<Users> aUser = Optional.of(uUser);
		RESTUser user = new RESTUser(uUser);		
		user.setUsername("user");
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);       
        when(userRepository.findByUsername(user.getUsername())).thenReturn(aUser);        
    	
    	SignUpRequest signUpRequest = new SignUpRequest();
    	signUpRequest.setUsername("userName");
    	signUpRequest.setPassword("password");    	
    	ResponseEntity<?> result = controller.registerUser(signUpRequest, mockedRequest);
    	assertEquals(result.getStatusCode(), HttpStatus.UNAUTHORIZED);    	
    }  
   
    @Test
    public void testRegisterUserNoAuthentication()
    {    	
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(null);       
    	
    	SignUpRequest signUpRequest = new SignUpRequest();
    	signUpRequest.setUsername("userName");
    	signUpRequest.setPassword("password");
    	
    	ResponseEntity<?> result = controller.registerUser(signUpRequest, mockedRequest);
    	assertEquals(result.getStatusCode(), HttpStatus.UNAUTHORIZED);    	
    }  
    
    private Users createAmdinUser ()
    {
    	Users adminUser = new Users();  
    	Role adminRole = new Role (RoleType.ROLE_ADMIN.toString());
    	adminUser.getRoles().add(adminRole);
    	return adminUser;
    }
    
    private Users createUser ()
    {
    	Users user = new Users();  
    	Role userRole = new Role (RoleType.ROLE_USER.toString());
    	user.getRoles().add(userRole);
    	return user;
    }

}
