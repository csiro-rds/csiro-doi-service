package au.csiro.doi.svc.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;


/**
 * @author pag06d
 *
 */
public class RestApiException extends RuntimeException
{
    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8208379579783065078L;

    /** the http status to return */
    private HttpStatus status;

    /** a list of errors to return */
    private List<ObjectError> errors;
    
    /**
     * Constructor
     * @param message
     *          A single error message
     * @param object
     *          The object the error occurred on
     * @param codes
     *          Custom error codes
     * @param arguments
     *          Arguments this error refers to
     * @param status
     *          The http status to return
     */
    public RestApiException(String message, String object, String[] codes, Object[] arguments, HttpStatus status)
    {
        super(message);
        errors = new ArrayList<ObjectError>();
        errors.add(new ObjectError(object, codes, arguments, message));
        this.status = status;
    }
    
    /**
     * Constructor
     * @param message
     *          A single error message
     * @param object
     *          The object the error refers to
     * @param status
     *          The http status
     */
    public RestApiException(String message, String object, HttpStatus status)
    {
        super(message);
        errors = new ArrayList<ObjectError>();
        errors.add(new ObjectError(object, message));
        this.status = status;
    }
    
    /**
     * Constructor
     * @param errors
     *          A List of errors
     * @param status
     *          The http status to return
     */
    public RestApiException(List<ObjectError> errors, HttpStatus status)
    {
        super();
        this.errors = errors;
        this.status = status;
    }
    

    /**
     * @return the status
     */
    public HttpStatus getStatus()
    {
        return status;
    }

    /**
     * @param status set the status
     */
    public void setStatus(HttpStatus status)
    {
        this.status = status;
    }

    /**
     * @return the list of errors
     */
    public List<ObjectError> getErrors()
    {
        return errors;
    }

    /**
     * @param errors set the list of errors
     */
    public void setErrors(List<ObjectError> errors)
    {
        this.errors = errors;
    }
    
    
    @Override
    public String toString()
    {
        return "RestApiException [status=" + status + ", errors=" + errors + "]";
    }
    
}
