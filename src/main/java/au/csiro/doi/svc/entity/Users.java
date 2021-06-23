/**
 * 
 */
package au.csiro.doi.svc.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import javax.persistence.Id;

/**
 * @author pag06d
 *
 */
@Entity
public class Users
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @SequenceGenerator(
            name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @OneToMany(
            fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Role> roles = new HashSet<>();

    /**
     * default constructor
     */
    public Users()
    {
    }

    /**
     * @param username the username
     * @param email the user email
     * @param password the user password
     */
    public Users(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }
}