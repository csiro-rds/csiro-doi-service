package au.csiro.doi.svc.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  Check status of database
 * @author san239 
 */
@Component
public class DbHealthCheck implements HealthIndicator 
{
	
    @Autowired
    private JdbcTemplate template;
    
    private static final int ERROR_CODE = 500;
    
    @Override
    public Health health() 
    {
        int errorCode = check(); // perform health check
        if (errorCode != 1) 
        {
            return Health.down().withDetail("Error Code", ERROR_CODE).build();
        }
        return Health.up().build();
    }

    private int check()
    {
        List<Object> results = template.query("select 1 from dual", new SingleColumnRowMapper<>());
        return results.size();
    }
}