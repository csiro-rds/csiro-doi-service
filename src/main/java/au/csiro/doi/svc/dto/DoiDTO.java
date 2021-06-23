/**
 * Copyright 2010, CSIRO Australia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package au.csiro.doi.svc.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Class representing an DOI Data Transfer Object. This class is serializable, so that it can easily be shared between
 * libraries.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author John Page on 03/03/2012
 * @version $Revision$ $Date$
 */
public class DoiDTO implements Serializable
{

    /** Generated serial version **/
    private static final long serialVersionUID = 5970619710155407372L;

    /** A persistent identifier that identifies a resource. **/
    private String identifier;

    /** Title of the Digital Object **/
    private String title;
    /**
     * The main researchers involved working on the data, or the authors of the publication in priority order. May be a
     * corporate/institutional or personal name.
     **/
    private List<Creator> creators;

    /** Publication Year **/
    private String publicationYear;
    /**
     * A holder of the data (including archives as appropriate) or institution which submitted the work. Any others may
     * be listed as contributors. This property will be used to formulate the citation, so consider the prominence of
     * the role.
     **/
    private String publisher;

    /** Subject **/
    private List<Subject> subjects;

    /** Primary language of the resource. Allowed values from: ISO 639-2/B, ISO 639-3 **/
    private String language;
    
    /**
     * Type of resource such as datasets and collections
     * associated workflows, software, models, grey literature
     */
    private String resourceType;
    
    /**
     * Resource type description
     */
    private String resourceTypeDescription;
    
    /**
     * Descriptions
     */
    private List<Description> descriptions;
    
    /**
     * Geo location data.  Can be a point or an area.
     */
    private List<GeoLocation> geoLocations;

    /**
     * Related identifiers
     */
    private List<RelatedIdentifier> relatedIdentifiers;
    
    public List<RelatedIdentifier> getRelatedIdentifiers()
    {
        return relatedIdentifiers;
    }

    public void setRelatedIdentifiers(List<RelatedIdentifier> relatedIdentifiers)
    {
        this.relatedIdentifiers = relatedIdentifiers;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * @return the creators
     */
    public List<Creator> getCreators()
    {
        return creators;
    }

    /**
     * @param creators the creators to set
     */
    public void setCreators(List<Creator> creators)
    {
        this.creators = creators;
    }

    public List<Subject> getSubjects()
    {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects)
    {
        this.subjects = subjects;
    }

    public List<Description> getDescriptions()
    {
        return descriptions;
    }

    public void setDescriptions(List<Description> descriptions)
    {
        this.descriptions = descriptions;
    }
    
    public List<GeoLocation> getGeoLocations()
    {
        return geoLocations;
    }

    public void setGeoLocations(List<GeoLocation> geoLocations)
    {
        this.geoLocations = geoLocations;
    }
    
    /**
     * @return the language
     */
    public String getLanguage()
    {
        return language;
    }
    
    /**
     * @param language
     *            the language to set
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * @return the publicationYear
     */
    public String getPublicationYear()
    {
        return publicationYear;
    }

    /**
     * @param publicationYear
     *            the publicationYear to set
     */
    public void setPublicationYear(String publicationYear)
    {
        this.publicationYear = publicationYear;
    }

    /**
     * @return the publisher
     */
    public String getPublisher()
    {
        return publisher;
    }

    /**
     * @param publisher
     *            the publisher to set
     */
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
    
    public String getResourceType()
    {
        return resourceType;
    }

    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }

    public String getResourceTypeDescription()
    {
        return resourceTypeDescription;
    }

    public void setResourceTypeDescription(String resourceTypeDescription)
    {
        this.resourceTypeDescription = resourceTypeDescription;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        final int maxLen = 1;
        StringBuilder builder = new StringBuilder();
        builder.append("DoiDTO [");
        if (identifier != null)
        {
            builder.append("identifier=").append(identifier).append(", ");
        }
        if (title != null)
        {
            builder.append("title=").append(title).append(", ");
        }
        if (creators != null)
        {
            builder.append("creators=").append(creators.subList(0, Math.min(creators.size(), maxLen))).append(", ");
        }
        if (publicationYear != null)
        {
            builder.append("publicationYear=").append(publicationYear).append(", ");
        }
        if (publisher != null)
        {
            builder.append("publisher=").append(publisher).append(", ");
        }
        if (subjects != null)
        {
            builder.append("subject=").append(subjects).append(", ");
        }
        if (language != null)
        {
            builder.append("language=").append(language).append(", ");
        }
        if (resourceType != null)
        {
            builder.append("resourceType=").append(resourceType).append(", ");
        }
        if (resourceTypeDescription != null)
        {
            builder.append("resourceTypeDescription=").append(resourceTypeDescription);
        }
        builder.append("]");
        return builder.toString();
    }
}
