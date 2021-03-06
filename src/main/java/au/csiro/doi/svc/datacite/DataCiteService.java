package au.csiro.doi.svc.datacite;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import au.csiro.doi.svc.controller.DoiServiceResponse;
import au.csiro.doi.svc.dto.DataCiteDoiDTO;
import au.csiro.doi.svc.dto.DoiDTO;
import au.csiro.doi.svc.util.DoiMetaDataGenerator;

/**
 * Provides the core DataCite services for managing DOIs.
 * 
 * Contains all logic for creating and executing the HTTP calls.
 * 
 * @author lin19b
 *
 */
@Component
public class DataCiteService
{

    private static final Logger LOGGER = LogManager.getLogger(DataCiteService.class);
   
    @Autowired
    private Environment env;
        
   
    
    /**
     * Constructs and executes an HTTP call.
     * 
     * @param requestMethod the method to call.
     * @param doiDTO        holds the objects for constructing updating the
     *                      meta-data XML.
     * @param doi           the doi
     * @param url           the url associated with the doi
     * @param event         doi state event, value is publish or hide
     * @throws HttpException thrown when attempting to execute method call.
     * @throws IOException   thrown when attempting to read response.
     * @return DoiServiceResponse the DOIService response.
     * 
     */
    public DoiServiceResponse executeMethod(RequestMethod requestMethod, DoiDTO doiDTO, String doi, String url,
            String event) throws HttpException, IOException 
    {
        
        // At this point, no validations should be required. 
        // All invalid parameters should have been caught earlier
      
        String metaDataXML = null;
        String encodedXML = null;
        String doiServiceUrl = env.getProperty("datacite.api.url");

        DataCiteDoiDTO dataCiteDoiDTO = new DataCiteDoiDTO();
        dataCiteDoiDTO.setType("dois");
        
        // url is required for both mint and update
        if (!StringUtils.isEmpty(url)) 
        {
            dataCiteDoiDTO.getAttributes().put("url", url);
        }

        
        // DOI will be null only if it is being minted in this call
        if (doi != null) 
        {
            doiServiceUrl = doiServiceUrl + "/" + doi;
        }

        if (requestMethod.equals(RequestMethod.POST)) 
        {
            // publish event to mint a findable DOI
            dataCiteDoiDTO.getAttributes().put("event", event);
            
            // supply DOI prefix, suffix will be auto generated by datacite
            String prefix = env.getProperty("doisvc.doi.prefix"); 
            dataCiteDoiDTO.getAttributes().put("prefix", prefix);

            // supply metadata in XML format
            metaDataXML = DoiMetaDataGenerator.generateMetaDataXMLFromDTO(doiDTO);
        }

        // Updates the meta-data xml with the parameters set in the DTO.
        if (requestMethod.equals(RequestMethod.PUT)) 
        {
            dataCiteDoiDTO.getAttributes().put("doi", doi);
            if (doiDTO != null) 
            {
                metaDataXML = DoiMetaDataGenerator.generateMetaDataXMLFromDTO(doiDTO);
            }
            if (event != null) 
            {
                // activate (publish) or deactivate (hide) event
                dataCiteDoiDTO.getAttributes().put("event", event);
            }
        }

        if (metaDataXML != null) 
        {
        	LOGGER.debug(" Generated metadata xml is {}", metaDataXML);
        	
            // XML metadata to be base64 encoded
            encodedXML = Base64.encodeBase64String(metaDataXML.getBytes(StandardCharsets.UTF_8));
            dataCiteDoiDTO.getAttributes().put("xml", encodedXML);
        }

        return doiRequest(doiServiceUrl, dataCiteDoiDTO, requestMethod);
    }

    /**
     * Calls a POST method of the DataCite DOI service in a
     * RESTful web service manner. The query string of the URI defines the type of
     * operation that is to be performed. The request body contains an XML fragment
     * that identifies the caller.
     *    
     * @param serviceUrl the URI of the RESTful DataCite DOI web service.
     * @param data          data to be sent
     * @param requestMethod the method to call.
     * @return service response
     * @throws HttpException thrown when attempting to execute method call.
     * @throws IOException   thrown when attempting to read response.
     */
    private DoiServiceResponse doiRequest(String serviceUrl, DataCiteDoiDTO data, RequestMethod requestMethod)
            throws HttpException, IOException 
    {
        if (LOGGER.isDebugEnabled()) 
        {
            LOGGER.debug("Method URL: " + serviceUrl);
            LOGGER.debug("Request Type: " + requestMethod);
        }
        DoiServiceResponse doiResponse = new DoiServiceResponse();
        
        ObjectMapper mapper = new ObjectMapper();

        String repository = env.getProperty("doisvc.app.repository"); 
        String password = env.getProperty("doisvc.app.password"); 
        
        HttpMethod httpMethod;    
        
        if (requestMethod.equals(RequestMethod.POST)) 
        {
            PostMethod postMethod = new PostMethod(serviceUrl.toString());
            if (data != null) 
            {
                // Converting the Object to JSONString
                String jsonString = mapper.writeValueAsString(data);
                jsonString = "{\"data\":" + jsonString + "}";
                StringRequestEntity requestEntity = new StringRequestEntity(jsonString, "application/json", "UTF-8");

                postMethod.setRequestEntity(requestEntity);
            }
            httpMethod = postMethod;            
        }

        else if (requestMethod.equals(RequestMethod.PUT)) 
        {
            PutMethod putMethod = new PutMethod(serviceUrl.toString());
            if (data != null) 
            {
                // Converting the Object to JSONString
                String jsonString = mapper.writeValueAsString(data);
                jsonString = "{\"data\":" + jsonString + "}";
                StringRequestEntity requestEntity = new StringRequestEntity(jsonString, "application/json", "UTF-8");

                putMethod.setRequestEntity(requestEntity);
            }         
            httpMethod = putMethod;
        }

        else if (requestMethod.equals(RequestMethod.DELETE)) 
        {
            httpMethod = new DeleteMethod(serviceUrl.toString());
        }

        // GET method, for a single DOI or a list of DOIs
        else 
        {
        	if (serviceUrl.endsWith("dois"))
        	{
        		//GET list of DOIs for this repository only
        		serviceUrl = serviceUrl+"?client-id="+ repository.toLowerCase();
        	}
            httpMethod = new GetMethod(serviceUrl.toString());
        }       

        String auth = repository + ":" + password;      
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
        httpMethod.setRequestHeader("Authorization", authHeader);
        httpMethod.setRequestHeader("Content-Type", "application/json");
        httpMethod.setRequestHeader("Accept", "application/json");

        try 
        {            
            HttpClient httpClient = new HttpClient();
            int responseCode = httpClient.executeMethod(httpMethod);
            if (LOGGER.isDebugEnabled()) 
            {
                LOGGER.debug(httpMethod.getStatusCode() + " - " + httpMethod.getStatusText());
            }

            // Create, Update, Get response 200 or 201
            if (responseCode == HttpStatus.OK.value() || responseCode == HttpStatus.CREATED.value()) 
            {                
                String dataResponse = httpMethod.getResponseBodyAsString();               

                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(dataResponse, JsonElement.class);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                JsonElement jsonData = jsonObject.get("data");
                if (jsonData instanceof JsonObject) 
                {
                    JsonElement jsonDoi = jsonData.getAsJsonObject().get("attributes").getAsJsonObject().get("doi");
                    String doi  =  jsonDoi.getAsString();
                    doiResponse.setDoi(doi);
                    
                    JsonElement jsonUrl = jsonData.getAsJsonObject().get("attributes").getAsJsonObject().get("url");
                    String url =  jsonUrl.getAsString();
                    doiResponse.setUrl(url);
                    
                    JsonElement xml = jsonData.getAsJsonObject().get("attributes").getAsJsonObject().get("xml");
                    byte[] metadata = Base64.decodeBase64(xml.getAsString());
                    doiResponse.setMetadata(new String(metadata));
                }
                else
                {
                    doiResponse.setMetadata(dataResponse);                    
                }

                //set success message
                if (requestMethod.equals(RequestMethod.POST))
                {
                    doiResponse.setMessage("The DOI was successfully minted.");
                }
                else if (requestMethod.equals(RequestMethod.GET))
                {
                    doiResponse.setMessage("The DOI was successfully retrieved.");
                }
                else
                {
                    doiResponse.setMessage("The DOI was successfully updated.");
                }
            }
            
            //delete successfully response 204
            else if (responseCode == HttpStatus.NO_CONTENT.value()) 
            {
                doiResponse.setMessage("The DOI was successfully deleted.");
            }
            
            else if (responseCode == HttpStatus.SERVICE_UNAVAILABLE.value())
            {
            	doiResponse.setMessage("The DataCite API server is currently unavailable to serve the request. "
            			+ "You may check https://status.datacite.org/ for DataCite service availability "
            			+ "and try again later.");
            }
            else
            {
                //errors
                String dataResponse = httpMethod.getResponseBodyAsString();
                
                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(dataResponse, JsonElement.class);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                JsonElement errors = jsonObject.get("errors");
                
                if (errors != null)
                {
                    JsonElement errorTitle = errors.getAsJsonArray().get(0).getAsJsonObject().get("title");             
                
                    doiResponse.setMessage(errorTitle.getAsString());
                }               
            }           

            if (LOGGER.isTraceEnabled()) 
            {
                LOGGER.trace("Response Message:" + doiResponse.getMessage());
            }
            setResponseFlag(responseCode, doiResponse);
        } 
        finally 
        {
            if (httpMethod != null) 
            {
                httpMethod.releaseConnection();
                httpMethod = null;
            }
        }

        return doiResponse;
    }   

    /**
     * Sets the success flag in DoiServiceResponse indicating success or failure
     * based on httpResponseCode
     * 
     * @param httpResponseCode http code returned from the web service invocation.
     * @param doiResponse      contains the response code and success flag
     * 
     */
    private static void setResponseFlag(int httpResponseCode, DoiServiceResponse doiResponse) 
    {
        doiResponse.setResponseCode(String.valueOf(httpResponseCode));
        if (httpResponseCode == HttpStatus.OK.value() || httpResponseCode == HttpStatus.CREATED.value()
                || httpResponseCode == HttpStatus.NO_CONTENT.value()) 
        {
            doiResponse.setSuccess(true);           
        } 
        else 
        {
            doiResponse.setSuccess(false);
        }
    }
}
