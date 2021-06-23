/**
 * 
 */
package au.csiro.doi.svc.db;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import au.csiro.doi.svc.entity.Role;

/**
 * @author pag06d
 *
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Long>
{
    /**
     * @param name the role name to find
     * @return the role
     */
    Optional<Role> findByName(String name);
}
