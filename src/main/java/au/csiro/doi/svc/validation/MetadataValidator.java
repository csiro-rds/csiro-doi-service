package au.csiro.doi.svc.validation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import au.csiro.doi.svc.controller.DoiController;
import au.csiro.doi.svc.controller.DoiServiceResponse;
import au.csiro.doi.svc.dto.DoiDTO;

/**
 * Validates the metadata parameters passed in a request.
 * 
 * @author pag06d
 *
 */
@Component
public class MetadataValidator
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DoiController.class);
    
    private static final String DOI_LINK = "https://doi.org/";
    
    private static final String VALID_DOI_PREFIX = DOI_LINK + "10.";
    
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());

    /**
     * Validate the DTO for mandatory parameters
     * 
     * @param doiDTO DTO with DOI request parameters.
     * @param url LandingPage url
     * 
     * @return DoiServiceResponse if validation fails, null if it passes
     */
    public DoiServiceResponse validateMetataParameters(DoiDTO doiDTO, String url)
    {
        if (doiDTO != null)
        {
            LOGGER.debug("DOIDTO parameters are {}", doiDTO.toString());
        }
         
        DoiServiceResponse doiResponse  = validateUrl(url);
        
        if (doiResponse != null)
        {
            LOGGER.error("Passed in URL was invalid {} ", url);
            return doiResponse;
        }
        
        if (doiDTO.getCreators() == null || doiDTO.getCreators().isEmpty())
        {
            doiResponse = new DoiServiceResponse();
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Atleast one creator should be set");
            doiResponse.setSuccess(false);
            return doiResponse;
        }
        
        if (StringUtils.isBlank(doiDTO.getTitle()))
        {
            doiResponse = new DoiServiceResponse();
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Title cannot be blank");
            doiResponse.setSuccess(false);
            return doiResponse;
        }
        
        if (!StringUtils.isNumeric(doiDTO.getPublicationYear()))
        {
            doiResponse = new DoiServiceResponse();
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Publication Year is invalid");
            doiResponse.setSuccess(false);
            return doiResponse;
        }

        
        if (StringUtils.isBlank(doiDTO.getPublisher()))
        {
            doiResponse = new DoiServiceResponse();
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Publisher cannot be blank");
            doiResponse.setSuccess(false);
            return doiResponse;
        }
        
        if (StringUtils.isBlank(doiDTO.getResourceType()))
        {
            doiResponse = new DoiServiceResponse();
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Resource type cannot be blank");
            doiResponse.setSuccess(false);
            return doiResponse;
        }
        
        return null;
        
    }

    private DoiServiceResponse validateUrl(String url)
    {
        if (StringUtils.isBlank(url))
        {
            DoiServiceResponse doiResponse = new DoiServiceResponse(); 
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Url cannot be blank");
            doiResponse.setSuccess(false);
            return doiResponse;
        }
        
        return null;
    }

    /**
     * Validates a passed in value as a DOI
     * 
     * @param doi
     *            Doi value that should belong to DataCite
     * @return DoiServiceResponse if validation fails, null if it passes
     */
    public DoiServiceResponse validateDOI(String doi)
    {
        if (StringUtils.isBlank(doi))
        {
            DoiServiceResponse doiResponse = new DoiServiceResponse();
            doiResponse.setResponseCode(BAD_REQUEST_CODE);
            doiResponse.setMessage("Doi cannot be blank");
            doiResponse.setSuccess(false);
            return doiResponse;
        }
        else
        {
            if (!(doi.startsWith(VALID_DOI_PREFIX) || doi.startsWith("10.")))
            {
                DoiServiceResponse doiResponse = new DoiServiceResponse();
                doiResponse.setResponseCode(BAD_REQUEST_CODE);
                doiResponse.setMessage("Not a datacite Doi");
                doiResponse.setSuccess(false);
                return doiResponse;
            }
        }
        return null;
    }

    /**
     * Validates the passed in Doi and Url
     * 
     * @param doi Doi
     * @param url Url that needs to be associated with the Doi
     * @return DoiServiceResponse if validation fails, null if it passes
     */
    public DoiServiceResponse validatUpdateParameters(String doi, String url)
    {
        DoiServiceResponse response = validateUrl(url);
        
        if (response == null)
        {
            response = validateDOI(doi);
        }
        return response;
    }


}
