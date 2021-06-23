package au.csiro.doi.svc.datacite;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Before;
import org.junit.Ignore;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;

import au.csiro.doi.svc.controller.DoiServiceResponse;
import au.csiro.doi.svc.dto.DoiDTO;
import au.csiro.doi.svc.services.RESTUser;
import au.csiro.doi.svc.services.RESTUserService;
import au.csiro.doi.svc.util.DoiMetadataTestUtils;

/**
 * Tests the {@link DataCiteService}
 * 
 * @author pag06d
 *
 */
@Ignore
public class DataCiteServiceTest
{

    @Mock
    private HttpServletRequest mockedRequest;
    
    @Mock
    private RESTUserService restUserService;    
    
    @Mock
    private Environment env;
    
    @Mock
    private HttpClient httpClient;
    
    @Mock
    private HttpMethod httpMethod;
    
    @InjectMocks
    private DataCiteService dataCiteService;
    
    private String dataResponse;
    
    
    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        
        dataResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/validDataCiteJSON.json")));
        mockedRequest = Mockito.mock(HttpServletRequest.class); 
        RESTUser user = mock(RESTUser.class);
        
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);
        when(env.getProperty("datacite.api.url")).thenReturn("https://api.test.datacite.org/dois");
        when(env.getProperty("doisvc.app.repository")).thenReturn("CSIRO.REPO");
        when(env.getProperty("doisvc.app.password")).thenReturn("5a27fa13bb");
        when(env.getProperty("doisvc.doi.prefix")).thenReturn("10.80413");
        when(httpMethod.getResponseBodyAsString()).thenReturn(dataResponse);
            
        Header contentType = new Header("Content-Type", "application/json; charset=utf-8");
        when(httpMethod.getResponseHeader("Content-Type")).thenReturn(contentType);
        
        
    }
    
    //This is a test calling the datacite test server
    @Ignore
    public void testExecuteHttpPostSuccess() throws HttpException, IOException
    {
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        DoiServiceResponse response = dataCiteService.executeMethod(RequestMethod.POST, dto, null, "http://www.test.com",
                "publish");     
        assertTrue("Doi should be present", response.getDoi().indexOf("10.80413")>-1);
    }
    //This is  test calling the datacite test server
    @Ignore
    public void testExecuteHttpPostFailure() throws HttpException, IOException
    {
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        DoiServiceResponse response = dataCiteService.executeMethod(RequestMethod.POST, dto, null, "www.test.com",
                "publish");
        
        assertTrue("URL is not valid", 
        		response.getMessage().indexOf("URL is not valid")>-1);
    }
    
}
