package au.csiro.doi.svc.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


/**
 * @author pag06d
 *
 */
@Entity
public class Role
{
    @Id 
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    @SequenceGenerator(
            name = "ROLE_SEQ", sequenceName = "ROLE_SEQ", allocationSize = 1)
    private Long id;

    private String name;

    /**
     * the default constructor
     */
    public Role()
    {

    }

    /**
     * @param name role name
     */
    public Role(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
