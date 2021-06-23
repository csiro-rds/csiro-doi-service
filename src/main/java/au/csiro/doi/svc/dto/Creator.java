package au.csiro.doi.svc.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

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
public class Creator implements Serializable
{
    /** Generated serial version **/
    private static final long serialVersionUID = 6854351518485871962L;
    
    private String firstName;
    private String lastName;
    private String nameType;
    private String affiliation;
    private String nameIdentifier;
    private String creatorName;
    
    
    /**
     * Name type: organizational. Attribute to describe type of creator 
     */
    public static final String NAME_TYPE_ORGANIZATIONAL = "Organizational";
    
    /**
     * Name type: personal. Attribute to describe type of creator 
     */
    public static final String NAME_TYPE_PERSONAL = "Personal";
    

    /**
     * Default constructor
     */
    public Creator()
    {
    }

    /**
     * Create Creator from first and last name and name type.
     * 
     * @param firstName
     *            The first name
     * @param lastName
     *            The last name
     * @param nameType
     *            The name type, either Personal or Organizational
     */
    public Creator(String firstName, String lastName, String nameType)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nameType = nameType;
    }
    
    /**
     * Create Creator from first and last name.
     * 
     * @param firstName
     *            The first name
     * @param lastName
     *            The last name
     */
    public Creator(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Get creator name, If not specified then last name + first name is returned.
     * 
     * @return Creator name.
     */
    public String getCreatorName()
    {
        if (StringUtils.isNotEmpty(creatorName))
        {
            return creatorName;
        }
        else if (StringUtils.isNotEmpty(lastName) && StringUtils.isNotEmpty(firstName))
        {
            return this.lastName + ", " + this.firstName;
        }
        return "";
    }
    
    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getNameType()
    {
        return nameType;
    }

    public void setNameType(String nameType)
    {
        this.nameType = nameType;
    }

    public String getAffiliation()
    {
        return affiliation;
    }

    public void setAffiliation(String affiliation)
    {
        this.affiliation = affiliation;
    }

    public String getNameIdentifier()
    {
        return nameIdentifier;
    }

    public void setNameIdentifier(String nameIdentifier)
    {
        this.nameIdentifier = nameIdentifier;
    }

}
