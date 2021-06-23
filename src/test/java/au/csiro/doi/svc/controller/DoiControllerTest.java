package au.csiro.doi.svc.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import au.csiro.doi.svc.datacite.DataCiteService;
import au.csiro.doi.svc.db.DoiResponseRepository;
import au.csiro.doi.svc.dto.DoiDTO;
import au.csiro.doi.svc.entity.DoiResponse;
import au.csiro.doi.svc.services.RESTUser;
import au.csiro.doi.svc.services.RESTUserService;
import au.csiro.doi.svc.util.DoiMetadataTestUtils;
import au.csiro.doi.svc.validation.MetadataValidator;



/**
 * Unit tests for the {@link DOIHandlingController}
 * 
 * @author pag06d
 *
 */

public class DoiControllerTest
{
    @InjectMocks
    private DoiController doiController;    
    
    @Mock
    private HttpServletRequest mockedRequest;
    
    @Mock
    private RESTUserService restUserService;    
    
    @Mock
    private MetadataValidator metadataValidator;
    
    @Mock
    private DataCiteService dataCiteService;
    
    @Mock
    private DoiResponseRepository doiResponseRepository;

    private DoiServiceResponse doiServiceResponse = new DoiServiceResponse();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
		mockedRequest = Mockito.mock(HttpServletRequest.class); 
		RESTUser user = mock(RESTUser.class);
		when(user.getId()).thenReturn("1");
        when(restUserService.getAuthenticatedUser(mockedRequest)).thenReturn(user);
    }
    

	@Test
	public void testMintDoiSuccess() throws Exception 
	{
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        doiServiceResponse.setResponseCode("201");
        doiServiceResponse.setDoi("10.80413/ef67-wh49");
        doiServiceResponse.setSuccess(true);        
   
        when(dataCiteService.executeMethod(RequestMethod.POST, dto, null, "www.test.com", "publish"))
                .thenReturn(doiServiceResponse);

        ResponseEntity<DoiServiceResponse> result = doiController.mintDoi(dto, "www.test.com", mockedRequest);

        DoiServiceResponse re = result.getBody();
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("10.80413/ef67-wh49", re.getDoi());
        assertEquals("201", re.getResponseCode());
        assertTrue("Doi was minted", re.isSuccess());        
        verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}	
	
    @Test
    public void testMintDoiFailure() throws Exception
    {
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        dto.setTitle(null);

        doiServiceResponse.setSuccess(false);
        doiServiceResponse.setResponseCode("400");
        doiServiceResponse.setMessage("Title cannot be blank");
        
        when(metadataValidator.validateMetataParameters(dto, "www.test.com")).thenReturn(doiServiceResponse);
       
        ResponseEntity<DoiServiceResponse> result = doiController.mintDoi(dto, "www.test.com", mockedRequest);

        DoiServiceResponse re = result.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(re.getResponseCode(), re.getResponseCode());
        assertEquals(re.getDoi(), null);
        assertFalse("Doi was not minted", re.isSuccess());        
        verify(doiResponseRepository, never()).save(any(DoiResponse.class));
    }
    
    @Test
	public void testUpdateDoiUrlSuccess() throws Exception 
	{
        doiServiceResponse.setResponseCode("200");
        doiServiceResponse.setDoi("10.80413/ef67-wh49");
        doiServiceResponse.setUrl("www.newurl.com");
        doiServiceResponse.setSuccess(true);
   
        when(dataCiteService.executeMethod(RequestMethod.PUT, null, "10.80413/ef67-wh49", "www.newurl.com", null))
                .thenReturn(doiServiceResponse);

        ResponseEntity<DoiServiceResponse> result = doiController.updateDoi("10.80413/ef67-wh49", 
        		"www.newurl.com", null, mockedRequest);

        DoiServiceResponse re = result.getBody();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("10.80413/ef67-wh49", re.getDoi());
        assertEquals("200", re.getResponseCode());
        assertEquals("www.newurl.com", re.getUrl());
        assertTrue("Doi was updated", re.isSuccess());    
        verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}	
    
    @Test
	public void testUpdateDoiUrlFailure() throws Exception 
	{
        doiServiceResponse.setResponseCode("400");
        doiServiceResponse.setDoi(null);
        doiServiceResponse.setUrl(null);
        doiServiceResponse.setSuccess(false);
        doiServiceResponse.setMessage("error message");
        
   
        when(dataCiteService.executeMethod(RequestMethod.PUT, null, "10.80413/ef67-wh49", "www.newurl.com", null))
                .thenReturn(doiServiceResponse);

        ResponseEntity<DoiServiceResponse> result = doiController.updateDoi("10.80413/ef67-wh49", 
        		"www.newurl.com", null, mockedRequest);

        DoiServiceResponse re = result.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse("error message", re.isSuccess());     
        verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}
    
    @Test
	public void testUpdateProgressiveDoiSuccess() throws Exception 
	{
        doiServiceResponse.setResponseCode("200");
        doiServiceResponse.setDoi("10.80413/ef67-wh49");
        doiServiceResponse.setUrl("www.newurl.com");
        doiServiceResponse.setSuccess(true);
        
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
   
        when(dataCiteService.executeMethod(RequestMethod.PUT, dto, "10.80413/ef67-wh49", "www.newurl.com", null))
                .thenReturn(doiServiceResponse);

        ResponseEntity<DoiServiceResponse> result = doiController.updateDoi("10.80413/ef67-wh49", 
        		"www.newurl.com", dto, mockedRequest);

        DoiServiceResponse re = result.getBody();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("10.80413/ef67-wh49", re.getDoi());
        assertEquals("200", re.getResponseCode());
        assertEquals("www.newurl.com", re.getUrl());
        assertTrue("Doi was updated", re.isSuccess());    
        verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}	
    
    @Test
	public void testUpdateProgressiveDoiFailure() throws Exception 
	{
        doiServiceResponse.setResponseCode("400");
        doiServiceResponse.setDoi(null);
        doiServiceResponse.setUrl(null);
        doiServiceResponse.setSuccess(false);
        doiServiceResponse.setMessage("error message");
        
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
   
        when(dataCiteService.executeMethod(RequestMethod.PUT, dto, "10.80413/ef67-wh49", "www.newurl.com", null))
                .thenReturn(doiServiceResponse);

        ResponseEntity<DoiServiceResponse> result = doiController.updateDoi("10.80413/ef67-wh49", 
        		"www.newurl.com", dto, mockedRequest);

        DoiServiceResponse re = result.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse("error message", re.isSuccess());     
        verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}	
    
    @Test
   	public void testActivateDoiSuccess() throws Exception 
   	{
           doiServiceResponse.setResponseCode("200");
           doiServiceResponse.setDoi("10.80413/ef67-wh49");
           doiServiceResponse.setUrl("www.newurl.com");
           doiServiceResponse.setSuccess(true);
      
           when(dataCiteService.executeMethod(RequestMethod.PUT, null, "10.80413/ef67-wh49", null, "publish"))
                   .thenReturn(doiServiceResponse);

           ResponseEntity<DoiServiceResponse> result = doiController.activateDoi("10.80413/ef67-wh49", mockedRequest);

           DoiServiceResponse re = result.getBody();
           assertEquals(HttpStatus.OK, result.getStatusCode());
           assertEquals("10.80413/ef67-wh49", re.getDoi());
           assertEquals("200", re.getResponseCode());
           assertEquals("www.newurl.com", re.getUrl());
           assertTrue("Doi was updated", re.isSuccess());       
           verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
   	}	
       
    @Test
   	public void testActivateDoiFailure() throws Exception 
   	{
           doiServiceResponse.setResponseCode("400");
           doiServiceResponse.setDoi(null);
           doiServiceResponse.setUrl(null);
           doiServiceResponse.setSuccess(false);
           doiServiceResponse.setMessage("error message");
           
      
           when(dataCiteService.executeMethod(RequestMethod.PUT, null, "10.80413/ef67-wh49", null, "publish"))
                   .thenReturn(doiServiceResponse);

           ResponseEntity<DoiServiceResponse> result = doiController.activateDoi("10.80413/ef67-wh49", mockedRequest);

           DoiServiceResponse re = result.getBody();
           assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
           assertFalse("error message", re.isSuccess());      
           verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}

	@Test
	public void testDeactivateDoiSuccess() throws Exception 
	{
		doiServiceResponse.setResponseCode("200");
		doiServiceResponse.setDoi("10.80413/ef67-wh49");
		doiServiceResponse.setUrl("www.newurl.com");
		doiServiceResponse.setSuccess(true);

		when(dataCiteService.executeMethod(RequestMethod.PUT, null, "10.80413/ef67-wh49", null, "hide"))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.deactivateDoi("10.80413/ef67-wh49", mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("10.80413/ef67-wh49", re.getDoi());
		assertEquals("200", re.getResponseCode());
		assertEquals("www.newurl.com", re.getUrl());
		assertTrue("Doi was updated", re.isSuccess());
		verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}

	@Test
	public void testDeactivateDoiFailure() throws Exception 
	{
		doiServiceResponse.setResponseCode("400");
		doiServiceResponse.setDoi(null);
		doiServiceResponse.setUrl(null);
		doiServiceResponse.setSuccess(false);
		doiServiceResponse.setMessage("error message");

		when(dataCiteService.executeMethod(RequestMethod.PUT, null, "10.80413/ef67-wh49", null, "hide"))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.deactivateDoi("10.80413/ef67-wh49", mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertFalse("error message", re.isSuccess());
		verify(doiResponseRepository, times(1)).save(any(DoiResponse.class));
	}
	
	@Test
	public void testDeleteDoiSuccess() throws Exception 
	{
		doiServiceResponse.setResponseCode("204");
		doiServiceResponse.setDoi(null);
		doiServiceResponse.setUrl(null);
		doiServiceResponse.setSuccess(true);

		when(dataCiteService.executeMethod(RequestMethod.DELETE, null, "10.80413/ef67-wh49", null, null))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.deleteDoi("10.80413/ef67-wh49", mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("204", re.getResponseCode());
		assertTrue("Doi was deleted", re.isSuccess());
	}

	@Test
	public void testDeleteDoiFailure() throws Exception 
	{
		doiServiceResponse.setResponseCode("400");
		doiServiceResponse.setDoi(null);
		doiServiceResponse.setUrl(null);
		doiServiceResponse.setSuccess(false);
		doiServiceResponse.setMessage("error message");

		when(dataCiteService.executeMethod(RequestMethod.DELETE, null, "10.80413/ef67-wh49", null, null))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.deleteDoi("10.80413/ef67-wh49", mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("400", re.getResponseCode());
		assertFalse("error message", re.isSuccess());
	}


	@Test
	public void testGetDoiSuccess() throws Exception 
	{
		doiServiceResponse.setResponseCode("200");
		doiServiceResponse.setDoi("10.80413/ef67-wh49");
		doiServiceResponse.setUrl("www.newurl.com");
		doiServiceResponse.setSuccess(true);
		doiServiceResponse.setMetadata("xml metadata");

		when(dataCiteService.executeMethod(RequestMethod.GET, null, "10.80413/ef67-wh49", null, null))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.getDoi("10.80413/ef67-wh49", mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("10.80413/ef67-wh49", re.getDoi());
		assertEquals("200", re.getResponseCode());
		assertEquals("www.newurl.com", re.getUrl());
		assertEquals("xml metadata", re.getMetadata());
		assertTrue("Doi was retrieved", re.isSuccess());
	}

	@Test
	public void testGetDoiFailure() throws Exception 
	{
		doiServiceResponse.setResponseCode("400");
		doiServiceResponse.setDoi(null);
		doiServiceResponse.setUrl(null);
		doiServiceResponse.setSuccess(false);
		doiServiceResponse.setMessage("error message");

		when(dataCiteService.executeMethod(RequestMethod.GET, null, "10.80413/ef67-wh49", null, null))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.getDoi("10.80413/ef67-wh49", mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertFalse("error message", re.isSuccess());
	}
	@Test
	public void testGetDoisSuccess() throws Exception 
	{
		doiServiceResponse.setResponseCode("200");
		doiServiceResponse.setDoi(null);
		doiServiceResponse.setUrl(null);
		doiServiceResponse.setSuccess(true);
		doiServiceResponse.setMessage("ok");
		doiServiceResponse.setMetadata("xml metadata");

		when(dataCiteService.executeMethod(RequestMethod.GET, null, null, null, null))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.getDois(mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("200", re.getResponseCode());
		assertEquals("xml metadata", re.getMetadata());
		assertTrue("Dois was retrieved", re.isSuccess());
	}

	@Test
	public void testGetDoisFailure() throws Exception 
	{
		doiServiceResponse.setResponseCode("400");
		doiServiceResponse.setDoi(null);
		doiServiceResponse.setUrl(null);
		doiServiceResponse.setSuccess(false);
		doiServiceResponse.setMessage("error message");
		doiServiceResponse.setMetadata(null);

		when(dataCiteService.executeMethod(RequestMethod.GET, null, null, null, null))
				.thenReturn(doiServiceResponse);

		ResponseEntity<DoiServiceResponse> result = doiController.getDois(mockedRequest);

		DoiServiceResponse re = result.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertFalse("error message", re.isSuccess());
	}
}
