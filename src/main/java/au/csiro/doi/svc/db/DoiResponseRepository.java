/**
 * 
 */
package au.csiro.doi.svc.db;

import org.springframework.data.repository.CrudRepository;

import au.csiro.doi.svc.entity.DoiResponse;
/**
 * @author san239
 *
 */
public interface DoiResponseRepository extends CrudRepository<DoiResponse, Long>
{   
}
