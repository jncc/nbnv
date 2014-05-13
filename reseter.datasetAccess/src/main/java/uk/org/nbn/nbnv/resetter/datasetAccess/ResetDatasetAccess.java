package uk.org.nbn.nbnv.resetter.datasetAccess;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import org.apache.log4j.Logger;
import uk.gov.nbn.data.properties.PropertiesReader;

/**
 *
 * @author Matt Debont
 */
public class ResetDatasetAccess {

    private Client client;
    private Properties props;
    private Cookie authCookie;
    private Map<String, Boolean[]> failureMap;

    static Logger log = Logger.getLogger(
            ResetDatasetAccess.class.getName());
    
    public ResetDatasetAccess() {
        failureMap = new HashMap<String, Boolean[]>();
        
        props = new Properties();
        try {
            props = PropertiesReader.getEffectiveProperties("datasetAccess.properties");
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
    }

    private List<String> getDatasets(String file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String input = br.readLine();

        List<String> datasets = new ArrayList<String>();

        while (input != null) {
            datasets.add(input);
            input = br.readLine();
        }

        return datasets;
    }

    private boolean resetDatasetAccess(List<String> datasets) {

        boolean retVal = true;
        
        for (String dataset : datasets) {
            WebResource webResource = client.resource(props.getProperty("api_url")
                    + "/user/userAccesses/reset/" + dataset);
            WebResource.Builder builder = webResource.getRequestBuilder();
            builder = builder.cookie(authCookie);
            
            log.debug("Reseting User Access for " + dataset);
            ClientResponse resp = builder.accept("application/json").get(ClientResponse.class);
            if (resp.getStatus() != 200) {
                log.debug("Failed to reset User Access for " + dataset + " :: " + resp.getEntity(String.class));
                retVal = false;
                Boolean[] arr = {false, true};
                failureMap.put(dataset, arr);
            }

            webResource = client.resource(props.getProperty("api_url")
                    + "/organisation/organisationAccesses/reset/" + dataset);
            builder = webResource.getRequestBuilder();
            builder = builder.cookie(authCookie);

            log.debug("Reseting Organisation Access for " + dataset);
            resp = builder.accept("application/json").get(ClientResponse.class);
            if (resp.getStatus() != 200) {
                log.debug("Failed to reset Organisation Access for " + dataset + " :: " + resp.getEntity(String.class));
                retVal = false;
                Boolean[] arr = {true, false};
                if (failureMap.containsKey(dataset)) {
                    arr[0] = false;
                }
                failureMap.put(dataset, arr);
            }
        }

        return retVal;
    }
    
    private Map<String, Boolean[]> getFailureMap() {
        return failureMap;
    }

    /**
     * Setup the mailer service and get it to poke the monthly reports
     * generation service
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            String path = args[0];
            ResetDatasetAccess resetDatasetAccess = new ResetDatasetAccess();
            try {
                List<String> datasets = resetDatasetAccess.getDatasets(path);
                log.debug("Retrieved Datasets...");
                log.debug("Got " + datasets);
                if (resetDatasetAccess.resetDatasetAccess(datasets)) {
                    System.exit(0);
                } else {
                    Map<String, Boolean[]> map = resetDatasetAccess.getFailureMap();
                    for (String dataset : map.keySet()) {
                        System.err.println("Failed " + dataset + " :: User " + map.get(dataset)[0] + " :: Organisation " + map.get(dataset)[1]);
                    }
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Could not find file at " + path + " :: " + ex.getLocalizedMessage());
                System.exit(1);
            } catch (IOException ex) {
                System.err.println("Problem opening file at " + path + " :: " + ex.getLocalizedMessage());
                System.exit(1);                
            }
            System.exit(0);
        } else {
            System.err.println("Usage: java -jar dataAccess.jar [path-to-dataset-list]");
            System.exit(1);
        }
    }
}