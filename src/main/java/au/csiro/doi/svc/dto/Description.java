package au.csiro.doi.svc.dto;

import java.io.Serializable;

/**
 * Class representing a Creator.
 * 
 * Can be an organisation or person.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author Chris Trapani on 13/11/2018
 * @version $Revision$ $Date$
 */
public class Description implements Serializable
{
    /** Generated serial version **/
    private static final long serialVersionUID = 2792195337953631667L;

    /**
     * Description.
     */
    private String description;

    /**
     * Description type.
     */
    private String descriptionType;

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescriptionType()
    {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType)
    {
        this.descriptionType = descriptionType;
    }

}
