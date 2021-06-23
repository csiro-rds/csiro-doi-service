package au.csiro.doi.svc.services;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import au.csiro.doi.svc.exception.RestApiException;

/**
 * @author pag06d
 *
 */
@Service
public class RESTUserServiceImpl implements RESTUserService
{
    
    /** Authorization */
    public static final String AUTH = "Authorization";
    
    /** Authorization minimum length*/
    public static final int AUTH_MIN_LEN = 6;
    
    /** The start of the Oath2 Authorisation header string */ 
    public static final String BEARER = "Bearer ";
    
    /** Error message for invalid field or values */
    public static final String INVALID_USER_PASS = "Invalid username or password";
    
    /** Logger info */
    private static final Logger LOGGER = LoggerFactory.getLogger(RESTUserServiceImpl.class);

    /**the authenticationManager
     * 
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    
    
    /**
     * Do basic authentication against the request header to see if it contains the user name and password of a valid
     * RESTUser
     * 
     * @param request
     *            The http servlet request   
     * @return The RESTUser if it is found
     * @throws RestApiException
     *             A rest api exception if rest user is not found
     */
    @Override
    public RESTUser getAuthenticatedUser(HttpServletRequest request) throws RestApiException
    {
        String authzHeader = null;
        
        try
        {
            authzHeader = request.getHeader(AUTH);
        } 
        catch (Exception e)
        {   
            return null;
        }
        
        String usernameAndPassword = StringUtils.EMPTY;
        
        if(StringUtils.isNotBlank(authzHeader) && authzHeader.length() > AUTH_MIN_LEN && 
                authzHeader.subSequence(0, AUTH_MIN_LEN).equals("Basic "))
        {
            usernameAndPassword = new String(Base64.getDecoder().decode(authzHeader.substring(AUTH_MIN_LEN).getBytes()));
            int userNameIndex = usernameAndPassword.indexOf(":");
            if(userNameIndex != -1)
            {   
                String userInfo [] = new String[2];
                userInfo[0] = usernameAndPassword.substring(0, userNameIndex);
                userInfo[1] = usernameAndPassword.substring(userNameIndex + 1);
                LOGGER.info("REST webservice request from username = {} ", userInfo[0] );
                
                Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(userInfo[0], userInfo[1]));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                RESTUser restUser = (RESTUser) authentication.getPrincipal();
                return restUser;
            }   
        }

        return null;
    }


	public AuthenticationManager getAuthenticationManager() 
	{
		return authenticationManager;
	}


	public void setAuthenticationManager(AuthenticationManager authenticationManager) 
	{
		this.authenticationManager = authenticationManager;
	}
    

}
