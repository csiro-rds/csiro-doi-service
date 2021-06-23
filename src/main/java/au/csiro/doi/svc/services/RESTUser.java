/**
 * 
 */
package au.csiro.doi.svc.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import au.csiro.doi.svc.entity.Role;
import au.csiro.doi.svc.entity.Users;

/**
 * 
 * @author pag06d
 *
 */
public class RESTUser implements UserDetails
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    
    private Set<Role> roles = new HashSet<>();
    
    private static final int HASH_START = 11;
    
    private static final int HASH_MULTIPLE = 13;

    /**
     * @param id user id
     * @param username username
     * @param email user email
     * @param password user password
     * @param authorities authorities
     */
    public RESTUser(String id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }


    /**
     * @param user the user
     */
    public RESTUser(Users user)
    {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }


    /**
     * @param user the user
     * @return a RESTUser
     */
    public static RESTUser build(Users user)
    {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new RESTUser(user.getId().toString(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    public String getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public String getPassword()
    {
        return password;
    }     

    public void setUsername(String username) 
    {
		this.username = username;
	}

	@Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        RESTUser user = (RESTUser) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode()    
    {
    	 int result = HASH_START;
         result = HASH_MULTIPLE * result + username.hashCode();
         result = HASH_MULTIPLE * result + email.hashCode();
         return result;
    	
    }

    /**
     * @return the roles
     */
    public Set<Role> getRoles()
    {
        return roles;
    }


    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }
}