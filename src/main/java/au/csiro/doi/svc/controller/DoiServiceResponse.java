package au.csiro.doi.svc.controller;

/**
 * Represents an Digital Object Identifier service response.
 * 
 * Copyright 2020, CSIRO Australia All rights reserved.
 * 
 * @author Xiangtan Lin on 06/09/2020
 * 
 *
 */
public class DoiServiceResponse 
{

	/**
	 * Any of the response codes from Cite My Data M2M service
	 */
	private String responseCode;

	/**
	 * Whether an ANDS DOI service call was successful.
	 */
	private boolean success;

	/**
	 * Digital Object Identifier returned by DOI service
	 */
	private String doi;
	
	
	/**
	 * URL associated with DOI
	 */
	private String url;

	/**
	 * Header of the message returned by the DOI service
	 */
	private String message;
	
	
	/**
	 * DOI metada if any
	 */
	private String metadata;

	/**
	 * Default constructor
	 */
	public DoiServiceResponse() 
	{

	}

	/**
	 * @return the doi
	 */
	public String getDoi() 
	{
		return doi;
	}

	/**
	 * @param doi the Digital Object Identifier to set
	 */
	public void setDoi(String doi) 
	{
		this.doi = doi;
	}

	
	public String getMetadata() 
	{
		return metadata;
	}

	public void setMetadata(String metadata) 
	{
		this.metadata = metadata;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() 
	{
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) 
	{
		this.success = success;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() 
	{
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) 
	{
		this.responseCode = responseCode;
	}

	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setMessage(String responseMessage) 
	{
		this.message = responseMessage;
	}

	/**
	 * @return the responseMessage
	 */
	public String getMessage() 
	{
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append("AndsDoiResponse [");
		if (doi != null) 
		{
			builder.append("doi=").append(doi).append(", ");
		}
		builder.append("success=").append(success).append(", ");
		if (responseCode != null) 
		{
			builder.append("responseCode=").append(responseCode).append(", ");
		}
		if (message != null) 
		{
			builder.append("\nmessage=\n").append(message);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getUrl() 
	{
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	

}
