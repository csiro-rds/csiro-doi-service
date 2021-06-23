/**
 * 
 */
package au.csiro.doi.svc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.csiro.doi.svc.db.UserRepository;
import au.csiro.doi.svc.entity.Users;

/**
 * @author pag06d
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    /**
     * the UserRepository
     */
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return RESTUser.build(user);
    }

	public UserRepository getUserRepository() 
	{
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) 
	{
		this.userRepository = userRepository;
	}
    

}
