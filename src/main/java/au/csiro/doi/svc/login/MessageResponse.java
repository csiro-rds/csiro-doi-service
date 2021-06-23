package au.csiro.doi.svc.login;

/**
 * @author pag06d
 *
 */
public class MessageResponse
{
    private String message;

    /**
     * @param message the message to return
     */
    public MessageResponse(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
