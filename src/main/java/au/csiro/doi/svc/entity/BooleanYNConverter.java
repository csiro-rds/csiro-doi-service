package au.csiro.doi.svc.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *  convert boolean field to character Y or N
 * @author san239 
 */
@Converter
public class BooleanYNConverter implements AttributeConverter<Boolean, String>
{	 
	@Override
	public String convertToDatabaseColumn(Boolean value) 
	{
	    if (value) 
	    {
	        return "Y";
	    } 
	    else 
	    {
	        return "N";
	    }
	}
	
	@Override
	public Boolean convertToEntityAttribute(String value) 
	{
	    return "Y".equals(value);
	}

}