/**
 * 
 */
package au.csiro.doi.svc.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import au.csiro.doi.svc.entity.Users;
/**
 * @author pag06d
 *
 */
public interface UserRepository extends JpaRepository<Users, String>
{
    /**
     * @param username the username to find
     * @return list of users
     */
    public Optional<Users> findByUsername(String username);

    /**
     * @param username username to check
     * @return true or false user exists
     */
    public Boolean existsByUsername(String username);

    /**
     * @param email email to check
     * @return true or false if user exists by email
     */
    public Boolean existsByEmail(String email);

}
