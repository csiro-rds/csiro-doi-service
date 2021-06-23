package au.csiro.doi.svc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.csiro.doi.svc.dto.DoiDTO;



/**
 * A utility class for generating a DoiMetaData XML file from an DoiDTO bean
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author John Page on 03/03/2011
 * @version $Revision$ $Date$
 */
public class DoiMetaDataGenerator
{
    
    /**
     * Constant giving the xpath of the creator name
     */
    private static final String IDENTIFIER_NAME_XPATH = "/xsi:resource/xsi:identifier";


    /**
     * Constant giving the xpath of the creator name
     */
    private static final String CREATOR_NAME_XPATH = "/xsi:resource/xsi:creators/xsi:creator/xsi:creatorName";

    /**
     * Constant giving the xpath of the creator name
     */
    public static final String GIVEN_NAME_XPATH = "/xsi:resource/xsi:creators/xsi:creator/xsi:givenName";

    /**
     * Constant giving the xpath of the creator name
     */
    public static final String FAMILY_NAME_XPATH = "/xsi:resource/xsi:creators/xsi:creator/xsi:familyName";
    
    /**
     * Constant giving the xPath of the object title name
     */
    private static final String TITLE_NAME_XPATH = "/xsi:resource/xsi:titles/xsi:title";

    /**
     * Constant giving the xPath of the publisher name
     */
    private static final String PUBLISHER_NAME_XPATH = "/xsi:resource/xsi:publisher";
    /**
     * Constant giving the xPath of the publication year
     */
    private static final String PUBLICATIONYEAR_NAME_XPATH = "/xsi:resource/xsi:publicationYear";
    
    /**
     * Constant giving the xPath of the Resource type
     */
    private static final String RESOURCETYPE_NAME_XPATH = "/xsi:resource/xsi:resourceType";

    /**
     * Constant giving the xPath of the related identifiers type
     */
    private static final String RELATED_IDENTIFIERS_XPATH = "/xsi:resource/xsi:relatedIdentifiers";
    
    /**
     * Constant giving the xPath of the related identifier type
     */
    private static final String RELATED_IDENTIFIER_XPATH = "/xsi:resource/xsi:relatedIdentifiers/xsi:relatedIdentifier";

    /**
     * Constant giving the xPath of the subjects type
     */
    private static final String SUBJECTS_XPATH = "/xsi:resource/xsi:subjects";
    
    /**
     * Constant giving the xPath of the subject type
     */
    private static final String SUBJECT_XPATH = "/xsi:resource/xsi:subjects/xsi:subject";
    
    /**
     * Constant giving the xPath of the related identifiers type
     */
    private static final String DESCRIPTIONS_XPATH = "/xsi:resource/xsi:descriptions";
    
    /**
     * Constant giving the xPath of the related identifier type
     */
    private static final String DESCRIPTION_XPATH = "/xsi:resource/xsi:descriptions/xsi:description";
    
    /**
     * Constant giving the xPath of the geo location type
     */
    private static final String GEO_LOCATION_XPATH = "/xsi:resource/xsi:geoLocations/xsi:geoLocation";

    /**
     * Constant giving the xPath of the geo locations type
     */
    private static final String GEO_LOCATIONS_XPATH = "/xsi:resource/xsi:geoLocations";

    /**
     * Constant giving the xPath of the related identifier type
     */
    public static final String RELATED_IDENTIFIER_TYPE = "relatedIdentifierType";
    
    /**
     * Constant giving the xPath of the related identifier type
     */
    public static final String RELATION_TYPE = "relationType";
    
    /**
     * Constant giving the xPath of the Resource type
     */
    public static final String RESOURCETYPE_GENERAL = "resourceTypeGeneral";
    
    /**
     * Description type for Description section
     */
    public static final String DESCRIPTION_TYPE = "descriptionType";

    /**
     * Affiliation for Creators section
     */
    public static final String AFFILIATION = "affiliation";

    /**
     * Name identifier for Creators section
     */
    public static final String NAME_IDENTIFIER = "nameIdentifier";

    /**
     * Family name for Creators section
     */
    public static final String FAMILY_NAME = "familyName";

    /**
     * Given name for Creators section
     */
    public static final String GIVEN_NAME = "givenName";

    /**
     * Name type for Creators section
     */
    public static final String NAME_TYPE = "nameType";
    
    /**
     * Constant giving the xPath of the Creator Name
     */
    private static final String NAME_SPACE = "xsi";

    /** Constant giving the path to the meta-data template **/
    private static final String METADATA_TEMPLATE_PATH = "/DoiMetadataTemplate.xml";
    
    /** Constant giving the path to the local copy of DataCite Metadata Schema **/
    private static final String SCHEMA_LOCALTION = "/schema/metadata.xsd";

    /**
     * Constant that defines the logger to be used.
     */
    private static final Logger LOG = LogManager.getLogger(DoiMetaDataGenerator.class);

    /**
     * Create a String representation of a DOIMetaData XML document from an DoiDTO bean using a template.
     * 
     * @param doiDto
     *            the bean representation of an OPAL record to be converted to a VOResource XML document
     * @return a String representation of the DOIMetaData XML
     * @throws IOException
     *             if the XML Document access fails
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     * @throws IllegalArgumentException
     *             If a parameter in doiDTO would violate the restrictions set on
     *             <a href="http://schema.datacite.org/meta/kernel-2.2/index.html">DataCite Metadata Schema v 2.2</a>
     * @throws JDOMException
     *             JDOMException
     */
    public static String createDoiMetaDataXML(DoiDTO doiDto) throws IOException,
            JDOMException
    {
        InputStream is = DoiMetaDataGenerator.class.getResourceAsStream(METADATA_TEMPLATE_PATH);
        
        SAXBuilder builder = createValidationSAXBuilder(SCHEMA_LOCALTION);

        // Implicit validation, it should not happen if the template is properly built
        Document document = builder.build(is);

        ConverterUtils.updateElementValuesForCreators(document, NAME_SPACE, CREATOR_NAME_XPATH, doiDto.getCreators());

        ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.TITLE_NAME_XPATH,
                doiDto.getTitle());
        ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.PUBLISHER_NAME_XPATH,
                doiDto.getPublisher());
        ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.PUBLICATIONYEAR_NAME_XPATH,
                doiDto.getPublicationYear());        
        ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.RESOURCETYPE_NAME_XPATH,
                doiDto.getResourceTypeDescription());
        ConverterUtils.updateAttribute(document, NAME_SPACE, DoiMetaDataGenerator.RESOURCETYPE_NAME_XPATH,
                RESOURCETYPE_GENERAL, doiDto.getResourceType());
        ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.RESOURCETYPE_NAME_XPATH,
                doiDto.getResourceTypeDescription());

        if (CollectionUtils.isNotEmpty(doiDto.getRelatedIdentifiers()))
        {
            ConverterUtils.updateElementValuesForRelatedIdentifiers(document, NAME_SPACE,
                    DoiMetaDataGenerator.RELATED_IDENTIFIER_XPATH, RELATED_IDENTIFIER_TYPE, RELATION_TYPE,
                    doiDto.getRelatedIdentifiers());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, DoiMetaDataGenerator.RELATED_IDENTIFIERS_XPATH);
        }

        if (CollectionUtils.isNotEmpty(doiDto.getDescriptions()))
        {
            ConverterUtils.updateElementValuesForDescriptions(document, NAME_SPACE, DESCRIPTION_XPATH,
                    doiDto.getDescriptions());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, DoiMetaDataGenerator.DESCRIPTIONS_XPATH);
        }
        
        if (CollectionUtils.isNotEmpty(doiDto.getGeoLocations()))
        {
            ConverterUtils.updateElementValuesForGeoLocations(document, NAME_SPACE, GEO_LOCATION_XPATH,
                    doiDto.getGeoLocations());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, DoiMetaDataGenerator.GEO_LOCATIONS_XPATH);
        }
        
        if (CollectionUtils.isNotEmpty(doiDto.getSubjects()))
        {
            ConverterUtils.updateElementValuesForSubjects(document, NAME_SPACE, SUBJECT_XPATH,
                    doiDto.getSubjects());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, SUBJECTS_XPATH);
        }
        
        String result = ConverterUtils.outputTheXml(document);

        try
        {
            document = builder.build(new StringReader(result));
        }
        catch (JDOMException e)
        {
            LOG.error(" Exception thrown when creating DOIMetadataXML {}", e);
            throw new IllegalArgumentException("It is most likely that one parameter in DoiDTO is invalid", e);
        }

        return result;
    }

    /**
     * @param schemaLocation
     * @return
     */
    private static SAXBuilder createValidationSAXBuilder(String schemaLocation)
    {
        java.net.URL url =
                DoiMetaDataGenerator.class.getResource(schemaLocation);
        
        SAXBuilder builder = new SAXBuilder(true);
        builder.setFeature("http://apache.org/xml/features/validation/schema", true); 
        
        builder.setProperty(
                "http://apache.org/xml/properties/schema/external-schemaLocation",
                url.toString());
        return builder;
    }

    /**
     * Updates a String representation of a DOIMetaData XML document from an DoiDTO bean using an existing metadata
     * document.
     * 
     * @param doiDto
     *            the bean representation of an OPAL record to be converted to a VOResource XML document
     * @param metaDataDocument the metadata document as a String           
     * @return String A string representation of the DOIMetaData XML
     * @throws IOException
     *             if the XML Document access fails
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static String updateDoiMetaDataXML(DoiDTO doiDto, String metaDataDocument) throws IOException,
            JDOMException
    {
        SAXBuilder builder = createValidationSAXBuilder(SCHEMA_LOCALTION);

        // Implicit validation, it should not happen if the template is properly built
        Document document = builder.build(new StringReader(metaDataDocument));


        if ((doiDto.getCreators() != null) && (doiDto.getCreators().size() > 0))
        {
            ConverterUtils.updateElementValuesForCreators(document, NAME_SPACE, CREATOR_NAME_XPATH,
                    doiDto.getCreators());
        }

        if (!StringUtils.isEmpty(doiDto.getTitle()))
        {
            ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.TITLE_NAME_XPATH,
                    doiDto.getTitle());
        }

        if (!StringUtils.isEmpty(doiDto.getPublisher()))
        {
            ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.PUBLISHER_NAME_XPATH,
                    doiDto.getPublisher());
        }

        if (!StringUtils.isEmpty(doiDto.getPublicationYear()))
        {
            ConverterUtils.updateElementValue(document, NAME_SPACE, DoiMetaDataGenerator.PUBLICATIONYEAR_NAME_XPATH,
                    doiDto.getPublicationYear());
        }

        if (!StringUtils.isEmpty(doiDto.getIdentifier()))
        {
            ConverterUtils.updateElementValue(document, NAME_SPACE, IDENTIFIER_NAME_XPATH, doiDto.getIdentifier());
        }

        if (CollectionUtils.isNotEmpty(doiDto.getRelatedIdentifiers()))
        {
            ConverterUtils.updateElementValuesForRelatedIdentifiers(document, NAME_SPACE,
                    DoiMetaDataGenerator.RELATED_IDENTIFIER_XPATH, RELATED_IDENTIFIER_TYPE, RELATION_TYPE,
                    doiDto.getRelatedIdentifiers());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, DoiMetaDataGenerator.RELATED_IDENTIFIERS_XPATH);
        }

        if (CollectionUtils.isNotEmpty(doiDto.getDescriptions()))
        {
            ConverterUtils.updateElementValuesForDescriptions(document, NAME_SPACE, DESCRIPTION_XPATH,
                    doiDto.getDescriptions());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, DoiMetaDataGenerator.DESCRIPTIONS_XPATH);
        }
        
        if (CollectionUtils.isNotEmpty(doiDto.getGeoLocations()))
        {
            ConverterUtils.updateElementValuesForGeoLocations(document, NAME_SPACE, GEO_LOCATION_XPATH,
                    doiDto.getGeoLocations());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, DoiMetaDataGenerator.GEO_LOCATIONS_XPATH);
        }
        
        if (CollectionUtils.isNotEmpty(doiDto.getSubjects()))
        {
            ConverterUtils.updateElementValuesForSubjects(document, NAME_SPACE, SUBJECT_XPATH,
                    doiDto.getSubjects());
        }
        else
        {
            // remove element as it is unused
            ConverterUtils.removeElement(document, NAME_SPACE, SUBJECTS_XPATH);
        }
        
        String result = ConverterUtils.outputTheXml(document);

        try
        {
            document = builder.build(new StringReader(result));
        }
        catch (JDOMException e)
        {
            LOG.error(" Exception thrown when creating DOIMetadataXML for update {}", e);
            throw new IllegalArgumentException("It is most likely that one parameter in DoiDTO is invalid", e);
        }

        return result;
    }

    
    /**
     * Sets the DOI value returned by the mint call and generates the meta-data xml.
     * 
     * @param doi
     *            The Digital Object Identifier that needs to be set into the xml.
     * @param xmlDocument
     *            XML document that needs to be updated.
     * @return string response.           
     * @throws IOException
     *             if the XML Document access fails
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static String updateDOI(String doi, String xmlDocument) throws JDOMException, IOException
    {
        Document document = ConverterUtils.xmlToDoc(xmlDocument);
        ConverterUtils.updateElementValue(document, NAME_SPACE, IDENTIFIER_NAME_XPATH, doi);

        return ConverterUtils.outputTheXml(document);

    }


    
    /**
     * Formats the DOI meta-data information into a XML format that the ANDS Digital Object Identifier service
     * understands.
     * @param doiDTO the DTO carrying the DOI information.
     * 
     * @return String representation of the XML metadata.
     * 
     */
    @SuppressWarnings(value = "all")
    public static String generateMetaDataXMLFromDTO(DoiDTO doiDTO)
    {
        String doiMetaDataXML = null;

        try
        {
            doiMetaDataXML = DoiMetaDataGenerator.createDoiMetaDataXML(doiDTO);
            LOG.debug(" DOI metadata xml generated is {}", doiMetaDataXML);
        }
        catch (IOException e)
        {
            LOG.error("Error in generating DOI Metadata for " + doiDTO.getTitle(), e);
        }
        catch (JDOMException ex)
        {
            LOG.error("Error in generating DOI Metadata for " + doiDTO.getTitle(), ex);
        }

        return doiMetaDataXML;

    }

    /**
     * Updates the meta-data xml associated with the DOI.
     * 
     * @param existingMetaDataXML
     *            existing metadata of the DOI.
     * @param doiDTO
     *            doiDTO with the values for the meta-data update.
     * @return String representation of the updated XML metadata.
     */
    public static String updateMetaDataXMLFromDTO(String existingMetaDataXML, DoiDTO doiDTO)
    {
        String doiMetaDataXML = null;

        try
        {
            doiMetaDataXML = DoiMetaDataGenerator.updateDoiMetaDataXML(doiDTO, existingMetaDataXML);
            LOG.debug(" DOI metadata xml generated is {}", doiMetaDataXML);
        }
        catch (IOException e)
        {
            LOG.error("Error in generating DOI Metadata for " + doiDTO.getTitle(), e);
        }
        catch (JDOMException ex)
        {
            LOG.error("Error in generating DOI Metadata for " + doiDTO.getTitle(), ex);
        }

        return doiMetaDataXML;
    }
}
