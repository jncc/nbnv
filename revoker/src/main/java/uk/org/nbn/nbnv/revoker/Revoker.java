package uk.org.nbn.nbnv.revoker;

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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import uk.gov.nbn.data.properties.PropertiesReader;
import uk.org.nbn.nbnv.revoker.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.revoker.model.UserAccessRequest;

/**
 *
 * @author Matt Debont
 */
public class Revoker {

    static Logger log = Logger.getLogger(
            Revoker.class.getName());
    private Client client;
    private Properties props;
    private Cookie authCookie;
    private BufferedWriter logfile;

    public Revoker() {
        props = new Properties();
        try {
            props = PropertiesReader.getEffectiveProperties("revoker.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("FAILURE: Could not find properties file");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("FAILURE: Unknown error occured");
            System.exit(1);
        }

        client = Client.create();

        System.out.println("Attempting to log into API....");

        WebResource webResource = client.resource(props.getProperty("api_url") + "/user/login");
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
        formData.add("username", props.getProperty("username"));
        formData.add("password", props.getProperty("password"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);

        if (response.getStatus() != 200) {
            throw new RuntimeException("FAILURE : API login with code : " + response.getStatus() + "\n     " + response.getEntity(String.class));
        }

        List<NewCookie> cookies = response.getCookies();
        for (NewCookie c : cookies) {
            if (c.getName().equals(props.get("sso_token_key"))) {
                authCookie = c;
            }
        }
        System.out.println("Logged into API....");

        log.info("Started Revokation");
    }

    private boolean expireUserRequests() {
        boolean retVal = true;
        WebResource webResource = client.resource(props.getProperty("api_url") + "/user/userAccesses/expired");
        WebResource.Builder builder = webResource.getRequestBuilder();
        builder = builder.cookie(authCookie);

        String requests =
                builder.accept(MediaType.APPLICATION_JSON).get(String.class);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<UserAccessRequest> reqs = mapper.readValue(requests, new TypeReference<List<UserAccessRequest>>() {});

            log.debug("Got " + reqs.size() + " expired requets from /user/userAccesses");
            
            for (UserAccessRequest request : reqs) {
                webResource = client.resource(props.getProperty("api_url")
                        + "/user/userAccesses"
                        + "/" + request.getFilter().getId());
                builder = webResource.getRequestBuilder();
                builder = builder.cookie(authCookie);

                log.debug("Revoking filterID " + request.getFilter().getId());

                ClientResponse resp = builder.accept("application/json").post(ClientResponse.class);
                if (resp.getStatus() != 200) {
                    log.error("Could not revoke filter with ID " + request.getFilter().getId());
                    log.error(resp.getEntity(String.class));
                    retVal = false;
                }

                log.debug("Revoked filterID " + request.getFilter().getId());
            }
        }
        catch(IOException ex) {
            log.error("Could not unmarshall JSON from server: \n" + ex.getLocalizedMessage());
        }

        return retVal;
    }

    private boolean expireOrganisationRequests() {
        boolean retVal = true;
        WebResource webResource = client.resource(props.getProperty("api_url") + "/organisation/organisationAccesses/expired");
        WebResource.Builder builder = webResource.getRequestBuilder();
        builder = builder.cookie(authCookie);

        String requests =
                builder.accept(MediaType.APPLICATION_JSON).get(String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<OrganisationAccessRequest> reqs = mapper.readValue(requests, new TypeReference<List<OrganisationAccessRequest>>() {});
            log.debug("Got " + reqs.size() + " expired requets from /organisation/organisationAccesses");

            for (OrganisationAccessRequest request : reqs) {
                webResource = client.resource(props.getProperty("api_url")
                        + "/organisation/organisationAccesses"
                        + "/" + request.getFilter().getId());
                builder = webResource.getRequestBuilder();
                builder = builder.cookie(authCookie);

                log.debug("Revoking filterID " + request.getFilter().getId());

            ClientResponse resp = builder.accept("application/json").post(ClientResponse.class);
            if (resp.getStatus() != 200) {
                log.error("Could not revoke filter with ID " + request.getFilter().getId());
                log.error(resp.getEntity(String.class));
                retVal = false;
            }

                log.debug("Revoked filterID " + request.getFilter().getId());
            }
        } catch(IOException ex) {
            log.error("Could not unmarshall JSON from server: \n" + ex.getLocalizedMessage());
        }

        return retVal;
    }

    public static void main(String[] args) {
        Revoker revoker = new Revoker();
        boolean users = revoker.expireUserRequests();
        boolean orgs = revoker.expireOrganisationRequests();
        if (users && orgs) {
            log.debug("Exiting without error");
            System.exit(0);
        } else {
            log.debug("Exiting with errors...");
            System.exit(1);
        }
    }
}
