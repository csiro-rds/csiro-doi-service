package au.csiro.doi.svc.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import au.csiro.doi.svc.datacite.DataCiteService;
import au.csiro.doi.svc.db.DoiResponseRepository;
import au.csiro.doi.svc.dto.DoiDTO;
import au.csiro.doi.svc.entity.DoiResponse;
import au.csiro.doi.svc.exception.RestApiException;
import au.csiro.doi.svc.services.RESTUser;
import au.csiro.doi.svc.validation.MetadataValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for handling DOI requests
 * 
 * @author lin19b
 *
 */
@Api(tags = "Api-V1 DOI Service")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class DoiController extends au.csiro.doi.svc.controller.RestController
{
	private static final Logger LOGGER = LogManager.getLogger(DoiController.class);

    /**
     * A publish event will move the DOI state to 'findable' 
     */
    private static final String DOI_EVENT_PUBLISH = "publish";

	
    /**
     * A hide event will move the DOI state to 'registered' 
     */
    private static final String DOI_EVENT_HIDE = "hide";

    @Autowired
    private MetadataValidator metadataValidator;
    
    @Autowired
    private DataCiteService dataCiteService;
    
    @Autowired
    private DoiResponseRepository doiResponseRepository;
       

    /**
     * Create a new DOI
     * 
     * @param doiDTO
     *            the metadata associated with the new DOI
     * @param url
     *            the url associated with the new DOI
     * @param request
     *            the http request
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation("Create a new DOI for a URL with metadata")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "doiDTO", value = "The metadata associated with the new DOI.", required = true,
                    dataType = "au.csiro.doi.svc.dto.DoiDTO", paramType = "body"),
            @ApiImplicitParam(name = "url", value = "The URL associated with the new DOI.", required = true,
                    dataType = "string", paramType = "query") })
    @RequestMapping(value = "/dois", method = RequestMethod.POST)
    public ResponseEntity<DoiServiceResponse> mintDoi(
    		@RequestBody(required = true) DoiDTO doiDTO,
            @RequestParam(value = "url", required = true) String url, 
            HttpServletRequest request)
            throws HttpException, IOException
    {
    	
        LOGGER.info("Minting a new DOI ");
    	
        RESTUser restUser = null;    	
    	try
    	{
    		restUser = validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "mintDoi");
    		return unauthorizedError(exception);
    	}      
    	DoiServiceResponse authResponse = metadataValidator.validateMetataParameters(doiDTO, url);
        if (authResponse != null && !authResponse.isSuccess())
        {
            LOGGER.error("DOIDTO parameters were invalid {} ", authResponse.getMessage());
            return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }
        
        DoiServiceResponse doiServiceResponse = dataCiteService.executeMethod(RequestMethod.POST, doiDTO, null, url,
                DOI_EVENT_PUBLISH);
        
        if (doiServiceResponse.isSuccess())
        {        	
            LOGGER.debug("DOIDTO minting successful {} ", doiServiceResponse.getMessage());
            saveDoiResponseParams(Long.parseLong(restUser.getId()), "mintDoi", true, doiServiceResponse);
            return new ResponseEntity<DoiServiceResponse>(doiServiceResponse, HttpStatus.CREATED);
        }     
        
        saveDoiResponseParams(Long.parseLong(restUser.getId()), "mintDoi", false, doiServiceResponse);        
        // If there is a failure return the appropriate code within the DOIResponse
        LOGGER.error("Failure to mint the DOI  {} ", doiServiceResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiServiceResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns a DOI
     * 
     * @param doi
     *            the doi to retrieve
     * @param request
     *            the httprequest
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation("Get metadata associated with a DOI")
    @RequestMapping(value = "/dois/{doi}", method = RequestMethod.GET)
    public ResponseEntity<DoiServiceResponse> getDoi(@PathVariable("doi") String doi, HttpServletRequest request)
            throws HttpException, IOException
    {   
        LOGGER.debug("Retrieving a metadata for doi {}", doi);
        
    	try
    	{
    		validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "getDoi");
    		return unauthorizedError(exception);
    	}     
    	
    	DoiServiceResponse authResponse = metadataValidator.validateDOI(doi);
        if (authResponse != null && !authResponse.isSuccess())
        {
            LOGGER.error("DOIDTO parameters were invalid {} ", authResponse.getMessage());
            return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }

        String doiString = doi.replace("~", "/");
        DoiServiceResponse doiResponse = dataCiteService.executeMethod(RequestMethod.GET, null, doiString, null,
                null);

        if (doiResponse.isSuccess())
        {
            return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.OK);
        }
        
        // If there is a failure return the appropriate code within the DOIResponse
        LOGGER.error("Failure to update the DOI  {} ", doiResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.BAD_REQUEST);
        
    }

    /**
     * Get a list of DOIs
     * 
     * @param request
     *            the httprequest
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation(hidden=true, value="Get a list of DOIs")
    @RequestMapping(value = "/dois", method = RequestMethod.GET)
    public ResponseEntity<DoiServiceResponse> getDois(HttpServletRequest request) throws HttpException, IOException
    {   
    	try
    	{
    		validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "getDois");
    		return unauthorizedError(exception);
    	}
       
        DoiServiceResponse doiResponse = dataCiteService.executeMethod(RequestMethod.GET, null, null, null, null);

        if (doiResponse.isSuccess())
        {
            return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.OK);
        }
        // If there is a failure return the appropriate code within the DOIResponse
        LOGGER.error("Failure to get a list of DOIs  {} ", doiResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Update a DOI for a new URL or existing URL and metadata
     * 
     * @param request
     *            the httprequest
     * @param doi
     *            the doi to be updated
     * @param doiDto
     *            the updated metadata for this doi
     * @param url
     *            the url for the doi
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation("Update a DOI for a new URL or existing URL and metadata")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(name = "url", value = "The new URL to be associated with the DOI.",
            required = true, dataType = "string", paramType = "query"), 
    	@ApiImplicitParam(name = "doiDTO", value = "The metadata associated with the new DOI.", required = false,
            dataType = "au.csiro.doi.svc.dto.DoiDTO", paramType = "body") })
    @RequestMapping(value = "/dois/{doi}", method = RequestMethod.PUT)
    public ResponseEntity<DoiServiceResponse> updateDoi(
    		@PathVariable("doi") String doi,
            @RequestParam(value = "url", required = true) String url, 
            @RequestBody(required = false) DoiDTO doiDto,
            HttpServletRequest request)
            throws HttpException, IOException
    {
    	RESTUser restUser = null;    	
    	try
    	{
    		restUser = validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "updateDoi");
    		return unauthorizedError(exception);
    	}   
    	DoiServiceResponse authResponse = metadataValidator.validatUpdateParameters(doi, url);
        if (authResponse != null && !authResponse.isSuccess())
        {
            LOGGER.error("Failure to Update the DOI {} ", authResponse.getMessage());
            return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }

        String doiString = doi.replace("~", "/");

        DoiServiceResponse doiResponse = dataCiteService.executeMethod(RequestMethod.PUT, doiDto, doiString, url, null);

        if (doiResponse.isSuccess())
        {
        	saveDoiResponseParams(Long.parseLong(restUser.getId()), "updateDoi", true, doiResponse);
            return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.OK);
        }
        
        saveDoiResponseParams(Long.parseLong(restUser.getId()), "updateDoi", false, doiResponse);        
       
        // If there is a failure return the appropriate code within the DOIResponse
        LOGGER.error("Failure to update DOI {} ", doiResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Deactivate a DOI
     * 
     * @param request
     *            the httprequest
     * @param doi
     *            the doi to be deactivated
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation("Deactivate a DOI")
    @RequestMapping(value = "/dois/{doi}/deactivate", method = RequestMethod.PUT)
    public ResponseEntity<DoiServiceResponse> deactivateDoi(@PathVariable("doi") String doi, HttpServletRequest request)
            throws HttpException, IOException
    {    	
    	RESTUser restUser = null;    	
    	try
    	{
    		restUser = validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "deactivateDoi");
    		return unauthorizedError(exception);
    	}   
    	DoiServiceResponse authResponse = metadataValidator.validateDOI(doi);
        if (authResponse != null && !authResponse.isSuccess())
        {
            LOGGER.error("Failure to Deactivate DOI {} ", authResponse.getMessage());
            return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }

        String doiString = doi.replace("~", "/");
        DoiServiceResponse doiServiceResponse = dataCiteService.executeMethod(RequestMethod.PUT, null, doiString, null,
                DOI_EVENT_HIDE);

        if (doiServiceResponse.isSuccess())
        {
        	saveDoiResponseParams(Long.parseLong(restUser.getId()), "deactivateDoi", true, doiServiceResponse);
            return new ResponseEntity<DoiServiceResponse>(doiServiceResponse, HttpStatus.OK);
        }
    	saveDoiResponseParams(Long.parseLong(restUser.getId()), "deactivateDoi", false, doiServiceResponse);   
    	
        // If there is a failure return the appropriate code within the DOIResponse
    	LOGGER.error("Failure to Deactivate DOI {} ", doiServiceResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiServiceResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Reactivate a DOI
     * 
     * @param request
     *            the httprequest
     * @param doi
     *            the doi to be reactivated
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation("Activate a DOI")
    @RequestMapping(value = "/dois/{doi}/activate", method = RequestMethod.PUT)
    public ResponseEntity<DoiServiceResponse> activateDoi(@PathVariable("doi") String doi, HttpServletRequest request)
            throws HttpException, IOException
    {    	
    	RESTUser restUser = null;    	
    	try
    	{
    		restUser = validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "activateDoi");
    		return unauthorizedError(exception);
    	}   
    	DoiServiceResponse authResponse = metadataValidator.validateDOI(doi);
        if (authResponse != null && !authResponse.isSuccess())
        {
            LOGGER.error("Failure to Activate a DOI {} ", authResponse.getMessage());
            return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }

        String doiString = doi.replace("~", "/");
        DoiServiceResponse doiServiceResponse = dataCiteService.executeMethod(RequestMethod.PUT, null, doiString, null,
                DOI_EVENT_PUBLISH);
        
        if (doiServiceResponse.isSuccess())
        {
        	saveDoiResponseParams(Long.parseLong(restUser.getId()), "activateDoi", true, doiServiceResponse);
            return new ResponseEntity<DoiServiceResponse>(doiServiceResponse, HttpStatus.OK);
        }
        saveDoiResponseParams(Long.parseLong(restUser.getId()), "activateDoi", false, doiServiceResponse);
        
        // If there is a failure return the appropriate code within the DOIResponse
        LOGGER.error("Failure to Activate a DOI {} ", doiServiceResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiServiceResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Delete a draft DOI
     * 
     * @param doi
     *            the doi to be deleted
     * @param request
     *            the HttpServletRequest
     * @return service response
     * @throws HttpException
     *             thrown when attempting to execute method call.
     * @throws IOException
     *             thrown when attempting to read response.
     */
    @ApiOperation(hidden=true, value = "Delete a draft DOI")
    @RequestMapping(value = "/dois/{doi}", method = RequestMethod.DELETE)
    public ResponseEntity<DoiServiceResponse> deleteDoi(@PathVariable("doi") String doi, HttpServletRequest request)
            throws HttpException, IOException
    {
    	try
    	{
    		validateUser(request);
    	}
    	catch(RestApiException exception)
    	{
    		LOGGER.error("Authorization failed to access the web service: ", "deleteDoi");
    		return unauthorizedError(exception);
    	}   

    	DoiServiceResponse authResponse = metadataValidator.validateDOI(doi);
        if (authResponse != null && !authResponse.isSuccess())
        {
            LOGGER.error("Failure to Delete a DRAFT DOI {} ", authResponse.getMessage());
            return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.BAD_REQUEST);
        }

        String doiString = doi.replace("~", "/");
        DoiServiceResponse doiResponse = dataCiteService.executeMethod(RequestMethod.DELETE, null, doiString, null,
                null);

        if (doiResponse.isSuccess())
        {
            return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.OK);
        }
        
        // If there is a failure return the appropriate code within the DOIResponse
        LOGGER.error("Failure to Delete a DRAFT DOI {} ", doiResponse.getMessage());
        return new ResponseEntity<DoiServiceResponse>(doiResponse, HttpStatus.BAD_REQUEST);
    }
    
    
    private DoiResponse saveDoiResponseParams(Long userId, String requestType, boolean requestResult, 
    		DoiServiceResponse doiServiceResponse)
    {
    	DoiResponse doiResponseParams = new DoiResponse(userId, requestType, requestResult, doiServiceResponse.getDoi(), 
    			doiServiceResponse.getUrl(), new Timestamp(System.currentTimeMillis()));    	
    	
        return doiResponseRepository.save(doiResponseParams);    	
    }

    private ResponseEntity<DoiServiceResponse> unauthorizedError(RestApiException exception)
    {
    	DoiServiceResponse authResponse = new DoiServiceResponse();
		authResponse.setSuccess(false);
		authResponse.setMessage(exception.getMessage());
		authResponse.setResponseCode(String.valueOf(exception.getStatus().value()));
		return new ResponseEntity<DoiServiceResponse>(authResponse, HttpStatus.UNAUTHORIZED);
    }
}
