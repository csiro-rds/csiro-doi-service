package au.csiro.doi.svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class for the Spring Boot Application
 * @author pag06d
 *
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Application extends SpringBootServletInitializer
{
    /**
     * @param args arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}