package au.csiro.doi.svc.controller;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

/**
 * @author Xiangtan Lin
 *
 */
public class TemplateControllerTest 
{
    @InjectMocks
    private TemplateController controller;
    @Mock
    private Model model;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetIndexSuccess()
    {
    	Model model = new BindingAwareModelMap();
    	controller.setActiveProfile("dev");
    	controller.setBuildNumber("xyz123");
    	String result = controller.index(model);
    	assertEquals(result,"index");
    	assertEquals(model.getAttribute("serverName"), "DEVELOPMENT");
    	assertEquals(model.getAttribute("buildNumber"), "xyz123");
    	
    	controller.setActiveProfile("test");
    	controller.setBuildNumber("xyz123");
    	result = controller.index(model);
    	assertEquals(result,"index");
    	assertEquals(model.getAttribute("serverName"), "TEST");
    	assertEquals(model.getAttribute("buildNumber"), "xyz123");
    	
    	controller.setActiveProfile("prod");
    	controller.setBuildNumber("xyz123");
    	result = controller.index(model);
    	assertEquals(result,"index");   	
    	assertEquals(model.getAttribute("serverName"), "PRODUCTION");
    	assertEquals(model.getAttribute("buildNumber"), "xyz123");
    }
    
    @Test
    public void testGetIndexFailure()
    {
    	expectedException.expect(ResourceNotFoundException.class);   
    	String result = controller.index(model);
    	
    	controller.setActiveProfile("dev");
    	expectedException.expect(ResourceNotFoundException.class);   
    	result = controller.index(model);
    	
    	controller.setActiveProfile("dev");
    	controller.setBuildNumber("xyz123");
    	result = controller.index(model);
    	assertEquals(result,"index");
    	 	
    }
}
