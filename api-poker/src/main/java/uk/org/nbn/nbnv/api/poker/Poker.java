package uk.org.nbn.nbnv.api.poker;

import org.apache.log4j.Logger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import uk.gov.nbn.data.properties.PropertiesReader;

/**
 *
 * @author Matt Debont
 */
public class Poker {

    static Logger log = Logger.getLogger(
            Poker.class.getName());
    private Client client;
    private Properties props;
    private Cookie authCookie;

    public Poker() {
        props = new Properties();
        try {
            props = PropertiesReader.getEffectiveProperties("poker.properties");
        } catch (FileNotFoundException ex) {
            log.error("FAILURE: Could not find properties file");
            System.exit(1);
        } catch (IOException ex) {
            log.error("FAILURE: Unknown error occured");
            System.exit(1);
        }

        client = Client.create();

        log.debug("Attempting to log into API....");

        WebResource webResource = client.resource(props.getProperty("api_url") + "/user/login");
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
        formData.add("username", props.getProperty("username"));
        formData.add("password", props.getProperty("password"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);

        if (response.getStatus() != 200) {
            log.error("API login with code : " + response.getStatus() + "\n     " + response.getEntity(String.class));
            System.exit(1);
        }

        List<NewCookie> cookies = response.getCookies();
        for (NewCookie c : cookies) {
            if (c.getName().equals(props.get("sso_token_key"))) {
                authCookie = c;
            }
        }
        log.debug("Logged into API....");

        log.info("Started Revokation");
    }
    
    private boolean pokeURLs() {
        String urlProps = props.getProperty("url");
        String[] urls = urlProps.split(",");
        boolean retVal = true;
        for (String url : urls) {
            log.info("Poking " + url);
            WebResource webResource = client.resource(props.getProperty("api_url") + url);
            WebResource.Builder builder = webResource.getRequestBuilder();
            builder = builder.cookie(authCookie);

            try {
                ClientResponse resp = builder.accept("application/json").get(ClientResponse.class);
                if (resp.getStatus() != 200) {
                    log.error("URL poke failed for " + url + " with the following message; " + resp.getEntity(String.class));
                    retVal = false;
                }
            } catch (Exception ex) {
                log.error("URL poke  for " + url + " returned an exception; " + ex.getLocalizedMessage());
                retVal = false;
            }
        }
        
        return retVal;
    }

    public static void main(String[] args) {
        Poker poker = new Poker();
        boolean success = poker.pokeURLs();
        if (success) {
            log.info("Exiting without error");
            System.exit(0);
        } else {
            log.error("Exiting with errors...");
            System.exit(1);
        }
    }
}