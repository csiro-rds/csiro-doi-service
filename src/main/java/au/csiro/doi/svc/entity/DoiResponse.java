/**
 * 
 */
package au.csiro.doi.svc.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author san239
 *
 */
@Entity
@Table(name = "DOI_RESPONSE_PARAMS") 
public class DoiResponse
{
	@Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, generator = "DOI_RES_SEQ")
    @SequenceGenerator(
            name = "DOI_RES_SEQ", sequenceName = "DOI_RES_SEQ", allocationSize = 1)
    private Long id;
	
    @NotBlank
    @Size(max = 20)
    private Long userId;
  
	@NotBlank
    @Size(max = 25)    
    private String requestType;

	@Column
    @Convert(converter=BooleanYNConverter.class)
    private boolean response;
    
    @Size(max = 255)    
    private String doi;

    @Size(max = 255)    
    private String url;
    
    @NotBlank        
    private Timestamp responseTime;


    /**
     * default constructor
     */
    public DoiResponse()
    {
    }

    /**
     * @param userId the userId
     * @param requestType requestType
     * @param response response
     * @param doi doi
     * @param url url
     * @param responseTime responseTime   
     */
    public DoiResponse(Long userId, String requestType, boolean response, String doi, String url, Timestamp responseTime)
    {
    	this.userId = userId;
    	this.requestType = requestType; 
    	this.response = response;
    	this.doi = doi;
    	this.url = url;
    	this.responseTime = responseTime;
    }

    public Long getId()
    {
        return id;
    }
   
    public void setId(Long id)
    {
        this.id = id;
    }
   
    public Long getUserId() 
    {
		return userId;
	}

	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}

	public String getRequestType() 
	{
		return requestType;
	}

	public void setRequestType(String requestType) 
	{
		this.requestType = requestType;
	}

	public boolean isResponse() 
	{
		return response;
	}

	public void setResponse(boolean response) 
	{
		this.response = response;
	}

	public String getDoi() 
	{
		return doi;
	}

	public void setDoi(String doi) 
	{
		this.doi = doi;
	}

	public String getUrl() 
	{
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = url;
	}

	public Timestamp getResponseTime() 
	{
		return responseTime;
	}

	public void setResponseTime(Timestamp responseTime) 
	{
		this.responseTime = responseTime;
	}  
 }