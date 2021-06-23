package au.csiro.doi.svc.util;

import java.util.ArrayList;
import java.util.List;

import au.csiro.doi.svc.dto.Creator;
import au.csiro.doi.svc.dto.Description;
import au.csiro.doi.svc.dto.DoiDTO;
import au.csiro.doi.svc.dto.GeoLocation;
import au.csiro.doi.svc.dto.RelatedIdentifier;
import au.csiro.doi.svc.dto.Subject;

/**
 * @author pag06d
 *
 */
public class DoiMetadataTestUtils
{

    public static DoiDTO createDummyDtoInclOptionalFields(boolean withNameTypes)
    {
        DoiDTO doiDTO = new DoiDTO();
        List<Creator> creators = withNameTypes? createDummyCreators(): createDummyCreatorsWithoutNameType();        

        // creators
        doiDTO.setCreators(creators);
        doiDTO.setTitle("A conceptual sediment budget for the Vietnam Shelf");
        doiDTO.setPublicationYear("2001");
        doiDTO.setPublisher("CSIRO");
        doiDTO.setResourceType("Text");
        doiDTO.setResourceTypeDescription("Reports");

        // related identifiers
        List<RelatedIdentifier> relatedIdentifiers = new ArrayList<>();
        addDummyRelatedIdentifier("URL", "Cites", "https://google.com", relatedIdentifiers);
        addDummyRelatedIdentifier("DOI", "Cites", "doi:10.1038/s41586-018-0588-y", relatedIdentifiers);
        addDummyRelatedIdentifier("DOI", "IsNewVersionOf", "doi:10.1038/s41586-018-0588-y", relatedIdentifiers);
        doiDTO.setRelatedIdentifiers(relatedIdentifiers);
        
        // description
        Description desc = new Description();
        desc.setDescription("description");
        desc.setDescriptionType("Abstract");
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(desc);
        doiDTO.setDescriptions(descriptions);

        // add geo points
        List<GeoLocation> geoLocations = new ArrayList<>();
        GeoLocation gl = new GeoLocation("-35.307380", "149.098210");
        geoLocations.add(gl);

        GeoLocation gl1 = new GeoLocation("-37.307380", "155.098210");
        geoLocations.add(gl1);

        // add geo area
        GeoLocation gl2 = new GeoLocation("-16.0", "-22.0", "145.0", "139.7");
        geoLocations.add(gl2);
        doiDTO.setGeoLocations(geoLocations);

        // subjects
        List<Subject> subjects = new ArrayList<>();
        Subject s = new Subject();
        s.setSubjectScheme("subjectScheme");
        s.setSchemeURI("schemeURI");
        s.setValueURI("valueUri");
        s.setSubject("subject");
        subjects.add(s);
        
        Subject s1 = new Subject();
        s1.setSubjectScheme("subjectScheme1");
        s1.setSchemeURI("schemeURI1");
        s1.setValueURI("valueUri1");
        s1.setSubject("subject1");
        subjects.add(s1);
        
        Subject s2 = new Subject();
        s2.setSubject("English");
        s2.setSubjectScheme("subjectScheme_English");
        s2.setSchemeURI("schemeURI_English");
        s2.setValueURI("valueUri_English");
        subjects.add(s2);
        
        Subject s3 = new Subject();
        s3.setSubject("Data Science");
        s3.setSubjectScheme("subjectScheme_Data Science");
        s3.setSchemeURI("schemeURI_Data Science\"");
        s3.setValueURI("valueUri_Data Science\"");
        subjects.add(s3);

        doiDTO.setSubjects(subjects);
        
        return doiDTO;

    }
    
    public static DoiDTO createDummyDoiDTOMandatoryFieldsOnly()
    {
        DoiDTO dto = new DoiDTO();
        
        List<Creator> creators = new ArrayList<Creator>();
        Creator creator1 = new Creator();
        String name = "Test User 1";
        creator1.setCreatorName(name);
        creator1.setNameType(Creator.NAME_TYPE_PERSONAL);
        creators.add(creator1);
        
        Creator creator2 = new Creator();
        name = "Test User 2";
        creator2.setCreatorName(name);
        creator2.setNameType(Creator.NAME_TYPE_PERSONAL);
        creators.add(creator2);
        
        Creator creator3 = new Creator();
        name = "Test User 3";
        creator3.setCreatorName(name);
        creator3.setNameType(Creator.NAME_TYPE_PERSONAL);
        creators.add(creator3);
        
        Creator creator4 = new Creator();
        name = "CSIRO";
        creator4.setCreatorName(name);
        creator4.setNameType(Creator.NAME_TYPE_ORGANIZATIONAL);
        creators.add(creator4);
        dto.setCreators(creators);
        
        dto.setTitle("Doi SVC is here");
        dto.setIdentifier("csiro:1000");
        dto.setPublisher("CSIRO");
        dto.setPublicationYear("2020");
        dto.setResourceType("Text");
        dto.setResourceTypeDescription("Reports");
        
        List<Description> descriptions = new ArrayList<Description>();
        Description desc = new Description();
        desc.setDescription("Abstract Desc");
        desc.setDescriptionType("Abstract");
        descriptions.add(desc);
        dto.setDescriptions(descriptions);

        return dto;
    }

    public static List<Creator> createDummyCreators()
    {
        List<Creator> creators = new ArrayList<Creator>();
        Creator c = new Creator();
        c.setCreatorName("CSIRO");
        c.setNameType("Organizational");
        creators.add(c);
        creators.add(new Creator("David", "Phillips", "Personal"));
        creators.add(new Creator("Rahul", "Gandhi", "Personal"));
        creators.add(new Creator("Alison", "Chapman", "Personal"));
        creators.add(new Creator("Matt", "Green", "Personal"));
        creators.add(new Creator("Rahul", "Dravid", "Personal"));
        creators.add(new Creator("John", "Smith", "Personal"));
        return creators;
    }
    
    public static List<Creator> createDummyCreatorsWithoutNameType()
    {
        List<Creator> creators = new ArrayList<Creator>();
        Creator c = new Creator();
        c.setCreatorName("CSIRO");
        creators.add(c);
        creators.add(new Creator("David", "Phillips"));
        creators.add(new Creator("Rahul", "Gandhi"));
        creators.add(new Creator("Alison", "Chapman"));
        creators.add(new Creator("Matt", "Green"));
        creators.add(new Creator("Rahul", "Dravid"));
        creators.add(new Creator("John", "Smith"));
        return creators;
    }


    public static DoiDTO populateDummydoiDTOMinimal()
    {
        DoiDTO doiDTO = new DoiDTO();
        List<Creator> creators = createDummyCreators();

        doiDTO.setCreators(creators);
        doiDTO.setTitle("A conceptual sediment budget for the Vietnam Shelf");
        doiDTO.setPublicationYear("2001");
        doiDTO.setPublisher("CSIRO");
        doiDTO.setResourceType("Text");
        doiDTO.setResourceTypeDescription("Reports");

        return doiDTO;
    }


    public static void addDummyRelatedIdentifier(String relatedIdentifierType, String relationType, String value,
            List<RelatedIdentifier> relatedIdentifiers)
    {
        RelatedIdentifier ri = new RelatedIdentifier();
        ri.setRelatedIdentifierType(relatedIdentifierType);
        ri.setRelationType(relationType);
        ri.setValue(value);
        relatedIdentifiers.add(ri);
    }
}
