package au.csiro.doi.svc.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import au.csiro.doi.svc.exception.RestApiException;
import au.csiro.doi.svc.services.RESTUser;
import au.csiro.doi.svc.services.RESTUserService;

/**
 * Rest Controller that manages Authentication
 * @author pag06d
 *
 */
public abstract class RestController
{

    private static final Logger LOGGER = LogManager.getLogger(RestController.class);
    
    @Autowired
    private RESTUserService restUserService;
    
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
    protected RESTUser validateUser(HttpServletRequest request) throws RestApiException
    {
        RESTUser restUser = restUserService.getAuthenticatedUser(request);
        
        if (restUser == null)
        {
            throw new RestApiException("Authentication is required to access this web service.", "RESTUser",
                    HttpStatus.UNAUTHORIZED);
        }
        
        LOGGER.debug("Retrieved Rest User {}", restUser.getUsername());
        return restUser;
    }
}
