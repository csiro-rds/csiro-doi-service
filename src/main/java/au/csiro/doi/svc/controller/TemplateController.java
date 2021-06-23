package au.csiro.doi.svc.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Xiangtan Lin
 *
 */
@ApiIgnore
@Controller
@PropertySource("classpath:version.properties")
public class TemplateController implements ErrorController
{
	@Value("${spring.profiles.active}")
	private String activeProfile;
	
	@Value("${build.number}")
	private String buildNumber;	
	   
    /**
     * @param model the ui model
     * @return the index template name
     */
    @GetMapping("/")
    public String index(Model model) 
    {        
    	if (StringUtils.isEmpty(activeProfile))
    	{
    		throw new ResourceNotFoundException("Active profiles is not set.");
    	}
    	
    	if (StringUtils.isEmpty(buildNumber))
    	{
    		throw new ResourceNotFoundException("Build number is not set.");
    	}    		
    	
    	// add attributes to template
    	model.addAttribute("serverName", getServerName(activeProfile));
        model.addAttribute("buildNumber", buildNumber);

        // return view name
        return "index";
    }
    
    @Override
	public String getErrorPath() 
    {
		return "/error";
	}  
    
    /**
     * @param model the ui model
     * @return the error template name
     */
    @GetMapping("/error")
    public String error(Model model) 
    {        
    	if (StringUtils.isEmpty(activeProfile))
    	{
    		throw new ResourceNotFoundException("Active profiles is not set.");
    	}
    	
    	if (StringUtils.isEmpty(buildNumber))
    	{
    		throw new ResourceNotFoundException("Build number is not set.");
    	}    		
    	
    	// add attributes to template
    	model.addAttribute("serverName", getServerName(activeProfile));
        model.addAttribute("buildNumber", buildNumber);

        // return view name
        return "error";
    }
    
    private String getServerName (String profile)
    {
    	String serverName = "DEVELOPMENT";
    	if (activeProfile!=null)
    	{
    		if (activeProfile.equals("test"))
    		{
    			serverName = "TEST";
    		}
    		if (activeProfile.equals("prod"))
    		{
    			serverName = "PRODUCTION";
    		}   		
    	}
    	return serverName;
    	
    }

	public String getActiveProfile() 
	{
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile) 
	{
		this.activeProfile = activeProfile;
	}

	public String getBuildNumber() 
	{
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) 
	{
		this.buildNumber = buildNumber;
	}    
}
