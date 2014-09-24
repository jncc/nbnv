package uk.org.nbn.nbnv.importer.daemon.config;

import java.io.IOException;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.gov.nbn.data.properties.PropertiesReader;

/**
 *
 * @author Matt Debont
 */
@Configuration
@ComponentScan(basePackages = "uk.org.nbn.nbnv.importer.daemon")
public class ApplicationConfig {     
    
    @Bean
    public Properties properties() throws IOException {
        return PropertiesReader.getEffectiveProperties("importer_daemon.properties");
    }
}
