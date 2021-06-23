package au.csiro.doi.svc.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.hamcrest.CoreMatchers;
import org.jdom.JDOMException;
import org.junit.Test;

import au.csiro.doi.svc.dto.DoiDTO;


/**
 * Tests the {@link DoiMetaDataGenerator}
 * @author pag06d
 *
 */
public class DoiMetaDataGeneratorTest
{
  
    private String doiDTOXml = "<resource xmlns=\"http://datacite.org/schema/kernel-4\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\"http://datacite.org/schema/kernel-4 "
            + "http://schema.datacite.org/meta/kernel-4.1/metadata.xsd\">\r\n" + 
            "  <identifier identifierType=\"DOI\">10.2312/meyniana.2005.57.101</identifier>\r\n" + 
            "  <creators>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Organizational\">CSIRO</creatorName>\r\n" + 
            "    </creator>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Personal\">Phillips, David</creatorName>\r\n" + 
            "      <givenName>David</givenName>\r\n" + 
            "      <familyName>Phillips</familyName>\r\n" + 
            "    </creator>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Personal\">Gandhi, Rahul</creatorName>\r\n" + 
            "      <givenName>Rahul</givenName>\r\n" + 
            "      <familyName>Gandhi</familyName>\r\n" + 
            "    </creator>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Personal\">Chapman, Alison</creatorName>\r\n" + 
            "      <givenName>Alison</givenName>\r\n" + 
            "      <familyName>Chapman</familyName>\r\n" + 
            "    </creator>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Personal\">Green, Matt</creatorName>\r\n" + 
            "      <givenName>Matt</givenName>\r\n" + 
            "      <familyName>Green</familyName>\r\n" + 
            "    </creator>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Personal\">Dravid, Rahul</creatorName>\r\n" + 
            "      <givenName>Rahul</givenName>\r\n" + 
            "      <familyName>Dravid</familyName>\r\n" + 
            "    </creator>\r\n" + 
            "    <creator>\r\n" + 
            "      <creatorName nameType=\"Personal\">Smith, John</creatorName>\r\n" + 
            "      <givenName>John</givenName>\r\n" + 
            "      <familyName>Smith</familyName>\r\n" + 
            "    </creator>\r\n" + 
            "  </creators>\r\n" + 
            "  <titles>\r\n" + 
            "    <title>Some Title</title>\r\n" + 
            "  </titles>\r\n" + 
            "  <publisher>CSIRO</publisher>\r\n" + 
            "  <publicationYear>2020</publicationYear>\r\n" + 
            "  <resourceType resourceTypeGeneral=\"Text\">Reports</resourceType>\r\n" + 
            "  <relatedIdentifiers>\r\n" + 
            "    <relatedIdentifier relatedIdentifierType=\"URL\" "
            + "relationType=\"Cites\">https://yahoo.com</relatedIdentifier>\r\n" + 
            "    <relatedIdentifier relatedIdentifierType=\"DOI\" "
            + "relationType=\"IsNewVersionOf\">doi:10.1038/s41586-018-0599-y</relatedIdentifier>\r\n" + 
            "    <relatedIdentifier relatedIdentifierType=\"DOI\" "
            + "relationType=\"Cites\">doi:10.1038/s41586-018-0599-y</relatedIdentifier>\r\n" + 
            "  </relatedIdentifiers>\r\n" + 
            "  <subjects>\r\n" + 
            "    <subject subjectScheme=\"subjectScheme\" schemeURI=\"schemeURI\" "
            + "valueURI=\"valueUri\">subject</subject>\r\n" + 
            "    <subject subjectScheme=\"subjectScheme_Data Science\" schemeURI=\"schemeURI_Data Science&quot;\" "
            + "valueURI=\"valueUri_Data Science&quot;\">Data Science</subject>\r\n" + 
            "    <subject subjectScheme=\"subjectScheme_English\" schemeURI=\"schemeURI_English\" "
            + "valueURI=\"valueUri_English\">English</subject>\r\n" + 
            "    <subject subjectScheme=\"subjectScheme1\" schemeURI=\"schemeURI1\" "
            + "valueURI=\"valueUri1\">subject1</subject>\r\n" + 
            "  </subjects>\r\n" + 
            "  <descriptions>\r\n" + 
            "    <description descriptionType=\"Abstract\">Abstract Description</description>\r\n" + 
            "  </descriptions>\r\n" + 
            "  <geoLocations>\r\n" + 
            "    <geoLocation>\r\n" + 
            "      <geoLocationPoint>\r\n" + 
            "        <pointLatitude>-35.307380</pointLatitude>\r\n" + 
            "        <pointLongitude>149.098210</pointLongitude>\r\n" + 
            "      </geoLocationPoint>\r\n" + 
            "    </geoLocation>\r\n" + 
            "    <geoLocation>\r\n" + 
            "      <geoLocationBox>\r\n" + 
            "        <northBoundLatitude>-16.0</northBoundLatitude>\r\n" + 
            "        <southBoundLatitude>-22.0</southBoundLatitude>\r\n" + 
            "        <eastBoundLongitude>145.0</eastBoundLongitude>\r\n" + 
            "        <westBoundLongitude>139.7</westBoundLongitude>\r\n" + 
            "      </geoLocationBox>\r\n" + 
            "    </geoLocation>\r\n" + 
            "    <geoLocation>\r\n" + 
            "      <geoLocationPoint>\r\n" + 
            "        <pointLatitude>-37.307380</pointLatitude>\r\n" + 
            "        <pointLongitude>155.098210</pointLongitude>\r\n" + 
            "      </geoLocationPoint>\r\n" + 
            "    </geoLocation>\r\n" + 
            "  </geoLocations>\r\n" + 
            "</resource>";
    
    @Test
    public void testCreateDoiMetadataXmlValid() throws Exception
    {
        String doiMetaDataXML = DoiMetaDataGenerator
                .createDoiMetaDataXML(DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly());
        
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("Doi SVC is here"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Test User 1</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Test User 2</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Test User 3</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Organizational\">CSIRO</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<publisher>CSIRO</publisher>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<publicationYear>2020</publicationYear>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<resourceType resourceTypeGeneral=\"Text\">Reports</resourceType>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString(" <description descriptionType=\"Abstract\">Abstract Desc</description>"));
    }
    
    @Test
    public void testGenerateMetaDataXMLFromDTOValid() throws Exception
    {
        String doiMetaDataXML = DoiMetaDataGenerator
                .createDoiMetaDataXML(DoiMetadataTestUtils.createDummyDtoInclOptionalFields(true));
        
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("A conceptual sediment budget for the Vietnam Shelf"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Phillips, David</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Gandhi, Rahul</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Chapman, Alison</creatorName>"));
        assertThat(doiMetaDataXML,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Green, Matt</creatorName>"));
    }
    
    @Test
    public void testCreateDoiMetadataXmlInValid() throws Exception
    {
        DoiDTO dto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        dto.setResourceType(null);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DoiMetaDataGenerator.createDoiMetaDataXML(dto);
        });
     
        String expectedMessage = "It is most likely that one parameter in DoiDTO is invalid";
        String actualMessage = exception.getMessage();
     
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testUpdateDOIValid() throws JDOMException, IOException
    {
        String doi = "10.5072/83/5820cf59d92dc";
        String updatedXml = DoiMetaDataGenerator.updateDOI(doi, doiDTOXml);
        assertThat(updatedXml, CoreMatchers
                .containsString("<identifier identifierType=\"DOI\">10.5072/83/5820cf59d92dc</identifier>"));
    }
    
    @Test
    public void updateDoiMetaDataXMLValid() throws IOException, JDOMException
    {
        assertThat(doiDTOXml,
                CoreMatchers.containsString("https://yahoo.com"));
        assertThat(doiDTOXml,
                CoreMatchers.containsString("Some Title"));
        assertThat(doiDTOXml,
                CoreMatchers.containsString("<publicationYear>2020</publicationYear>"));
        assertThat(doiDTOXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Phillips, David</creatorName>"));
        assertThat(doiDTOXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Gandhi, Rahul</creatorName>"));
        assertThat(doiDTOXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Chapman, Alison</creatorName>"));
        assertThat(doiDTOXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Green, Matt</creatorName>"));
        
        //Values to be added/changed
        DoiDTO doiDTO = DoiMetadataTestUtils.createDummyDtoInclOptionalFields(true);
        String updatedXml = DoiMetaDataGenerator.updateMetaDataXMLFromDTO(doiDTOXml, doiDTO);

        //Updated values
        assertThat(updatedXml,
                CoreMatchers.containsString("A conceptual sediment budget for the Vietnam Shelf"));
        assertThat(updatedXml,
                CoreMatchers.containsString("<publicationYear>2001</publicationYear>"));
        assertThat(updatedXml,
                CoreMatchers.containsString("https://google.com"));
        
        //Unchanged values
        assertThat(updatedXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Phillips, David</creatorName>"));
        assertThat(updatedXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Gandhi, Rahul</creatorName>"));
        assertThat(updatedXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Chapman, Alison</creatorName>"));
        assertThat(updatedXml,
                CoreMatchers.containsString("<creatorName nameType=\"Personal\">Green, Matt</creatorName>"));

    }
    
   
}
