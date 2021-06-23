package au.csiro.doi.svc.login;

import java.util.List;

/**
 * @author pag06d
 *
 */
public class TokenResponse
{
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private List<String> roles;

    /**
     * @param accessToken the accessToken
     * @param id the user id
     * @param username the username
     * @param email the user email
     * @param roles the user roles
     */
    public TokenResponse(String accessToken, String id, String username, String email, List<String> roles)
    {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken()
    {
        return token;
    }

    public void setAccessToken(String accessToken)
    {
        this.token = accessToken;
    }

    public String getTokenType()
    {
        return type;
    }

    public void setTokenType(String tokenType)
    {
        this.type = tokenType;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public List<String> getRoles()
    {
        return roles;
    }
}
