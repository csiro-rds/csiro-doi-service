package au.csiro.doi.svc.login;

import java.util.HashSet;
/**
 * @author pag06d
 *
 */
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * 
 * @author pag06d
 *
 */
public class SignUpRequest
{
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role = new HashSet<String>();

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Set<String> getRoles()
    {
        return this.role;
    }

    public void setRoles(Set<String> role)
    {
        this.role = role;
    }
    
}