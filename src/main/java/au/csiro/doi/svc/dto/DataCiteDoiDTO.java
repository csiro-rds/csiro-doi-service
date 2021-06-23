package au.csiro.doi.svc.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an DOI Data Transfer Object. This class is serializable, so that it can easily be shared between
 * libraries.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author John Page on 03/03/2012
 */
public class DataCiteDoiDTO implements Serializable
{
    private static final long serialVersionUID = 4930812496632619491L;
    private String type = "dois";
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Map<String, Object> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes)
    {
        this.attributes = attributes;
    }
}
