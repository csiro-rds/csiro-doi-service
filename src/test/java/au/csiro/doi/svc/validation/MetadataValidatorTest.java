/**
 * 
 */
package au.csiro.doi.svc.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import au.csiro.doi.svc.controller.DoiServiceResponse;
import au.csiro.doi.svc.dto.DoiDTO;
import au.csiro.doi.svc.util.DoiMetadataTestUtils;

/**
 * Tests the {@link MetadataValidator}
 * 
 * @author pag06d
 *
 */
public class MetadataValidatorTest
{
    private MetadataValidator validator = new MetadataValidator();

    @Test
    public void testMetadataValidatorParametersUrlInValid()
    {
        DoiServiceResponse response = validator.validateMetataParameters(new DoiDTO(), null);
        assertEquals("Validation failure expected", "Url cannot be blank", response.getMessage());
        assertEquals("Doi should not be present", null, response.getDoi());
    }

    @Test
    public void testMetadataValidatorParametersTitleInValid()
    {
        DoiDTO doiDto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        doiDto.setTitle(null);

        DoiServiceResponse response = validator.validateMetataParameters(doiDto, "https://somewhere.com");
        assertEquals("Validation failure expected", "Title cannot be blank", response.getMessage());
        assertEquals("Doi should not be present", null, response.getDoi());
    }

    @Test
    public void testMetadataValidatorParametersResourceTypeInValid()
    {
        DoiDTO doiDto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        doiDto.setResourceType(null);

        DoiServiceResponse response = validator.validateMetataParameters(doiDto, "https://somewhere.com");
        assertEquals("Validation failure expected", "Resource type cannot be blank", response.getMessage());
        assertEquals("Doi should not be present", null, response.getDoi());
    }

    @Test
    public void testMetadataValidatorParametersPublicationDateInValid()
    {
        DoiDTO doiDto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        doiDto.setPublicationYear(null);

        DoiServiceResponse response = validator.validateMetataParameters(doiDto, "https://somewhere.com");
        assertEquals("Validation failure expected", "Publication Year is invalid", response.getMessage());
        assertEquals("Doi should not be present", null, response.getDoi());

        doiDto.setPublicationYear("something");
        response = validator.validateMetataParameters(doiDto, "https://somewhere.com");
        assertEquals("Validation failure expected", "Publication Year is invalid", response.getMessage());
        assertEquals("Doi should not be present", null, response.getDoi());
    }

    @Test
    public void testMetadataValidatorParametersValid()
    {
        DoiDTO doiDto = DoiMetadataTestUtils.createDummyDoiDTOMandatoryFieldsOnly();
        DoiServiceResponse response = validator.validateMetataParameters(doiDto, "https://somewhere.com");
        assertEquals("No failure expected", null, response);
    }

    @Test
    public void testMetadataValidatorDoiInValid()
    {
        DoiServiceResponse response = validator.validateDOI(null);
        assertEquals("Validation failure expected", "Doi cannot be blank", response.getMessage());
        assertFalse("Doi response indicates failure", response.isSuccess());
    }

    @Test
    public void testMetadataValidatorDoiPrefixInvalid()
    {
        DoiServiceResponse response = validator.validateDOI("1234.10");
        assertEquals("Validation failure expected", "Not a datacite Doi", response.getMessage());
        assertFalse("Doi response indicates failure", response.isSuccess());

        response = validator.validateDOI("https://doi.org/1234.212");
        assertEquals("Validation failure expected", "Not a datacite Doi", response.getMessage());
        assertFalse("Doi response indicates failure", response.isSuccess());

        response = validator.validateDOI("https://doi.org/10.1234.212");
        assertEquals("No failure expected", null, response);
    }

    @Test
    public void testMetadataValidatorDoiPrefixValid()
    {
        DoiServiceResponse response = validator.validateDOI("1234.10");
        response = validator.validateDOI("10.1234.212");
        assertEquals("No failure expected", null, response);

        response = validator.validateDOI("https://doi.org/10.1234.212");
        assertEquals("No failure expected", null, response);
    }
    
}
