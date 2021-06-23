package au.csiro.doi.svc.services;

import javax.servlet.http.HttpServletRequest;



/**
 * 
 * Interface for ReST user operations.
 * <p>
 * Copyright 2015, CSIRO Australia. All rights reserved.
 */
public interface RESTUserService
{

    /**
     * Do basic authentication against a servlet request depending on the value if the security header
     * @param request
     *              The web service request
     * @return
     *              The authenticated user or null if authentication failed
     */
    public RESTUser getAuthenticatedUser(HttpServletRequest request);
    
    
        
}
