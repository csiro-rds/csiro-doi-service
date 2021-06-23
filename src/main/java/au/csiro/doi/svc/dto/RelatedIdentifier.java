package au.csiro.doi.svc.dto;

import java.io.Serializable;

/**
 * Class representing an Related identifier.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author Chris Trapani on 13/11/2018
 * @version $Revision$ $Date$
 */
public class RelatedIdentifier implements Serializable
{
    /** Generated serial version **/
    private static final long serialVersionUID = -3661963648667782037L;
    
    private String relatedIdentifierType;
    private String relationType;
    private String value;

    public String getRelatedIdentifierType()
    {
        return relatedIdentifierType;
    }

    public void setRelatedIdentifierType(String relatedIdentifierType)
    {
        this.relatedIdentifierType = relatedIdentifierType;
    }

    public String getRelationType()
    {
        return relationType;
    }

    public void setRelationType(String relationType)
    {
        this.relationType = relationType;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}
