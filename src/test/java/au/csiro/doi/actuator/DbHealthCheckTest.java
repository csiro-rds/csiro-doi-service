package au.csiro.doi.actuator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.springframework.boot.actuate.health.Health;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import au.csiro.doi.svc.actuator.DbHealthCheck;




/**
 * Tests the {@link DbHealthCheck}
 * 
 * @author san239
 *
 */
public class DbHealthCheckTest
{

	@Mock 
	JdbcTemplate template;
	
	@InjectMocks
	DbHealthCheck dbHealthCheck;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

	@SuppressWarnings("unchecked")
	@Test
	public void testdbHealthSuccess() 
	{	
		List<Object> list = new ArrayList<>();
		list.add(1);		
		when(template.query(anyString(), any(SingleColumnRowMapper.class))).thenReturn(list);
		
		Health health = dbHealthCheck.health();
		
		assertEquals("Database is not up", "UP", health.getStatus().getCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testdbHealthFailure() 
	{	
		List<Object> list = new ArrayList<>();
		when(template.query(anyString(), any(SingleColumnRowMapper.class))).thenReturn(list);
		
		Health health = dbHealthCheck.health();
		
		assertEquals("Database is up", "DOWN", health.getStatus().getCode());
	}
}
