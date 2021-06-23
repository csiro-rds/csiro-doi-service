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
public class Subject implements Serializable
{
    /** Generated serial version **/
    private static final long serialVersionUID = 8619853034995761718L;
    
    private String subject;
    private String subjectScheme;
    private String schemeURI;
    private String valueURI;

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getSubjectScheme()
    {
        return subjectScheme;
    }

    public void setSubjectScheme(String subjectScheme)
    {
        this.subjectScheme = subjectScheme;
    }

    public String getSchemeURI()
    {
        return schemeURI;
    }

    public void setSchemeURI(String schemeURI)
    {
        this.schemeURI = schemeURI;
    }

    public String getValueURI()
    {
        return valueURI;
    }

    public void setValueURI(String valueURI)
    {
        this.valueURI = valueURI;
    }

}
