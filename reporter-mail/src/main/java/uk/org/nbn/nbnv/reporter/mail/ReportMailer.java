/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.reporter.mail;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

/**
 *
 * @author Matt Debont
 */
public class ReportMailer {

    private Client client;
    private Properties props;
    private Cookie authCookie;

    public ReportMailer() {
        props = new Properties();
        try {
            props.load(new FileInputStream(new File("reportMailer.properties")));
        } catch (FileNotFoundException ex) {
            System.err.println("FAILURE: Could not find properties file, reportMailer.properties needs to located in the same folder as the jar");
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

    /**
     * Send out download reports for the current month
     */
    public void pokeEmailService() {
        System.out.println("Poking API....");
        Calendar cal = Calendar.getInstance();
        processEmails(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    /**
     * Send out download reports for the specified month (1-12)
     * @param month A month (1-12)
     */
    public void pokeEmailService(int month) {
        System.out.println("Poking API with month " + month + "....");
        Calendar cal = Calendar.getInstance();
        processEmails(month - 1, cal.get(Calendar.YEAR));
    }

    /**
     * Send out download reports for the specified month (1-12) and year
     * @param month A month (1-12)
     * @param year A year
     */
    public void pokeEmailService(int month, int year) {
        System.out.println("Poking API with month " + month + " and year " + year + "....");
        processEmails(month - 1, year);
    }

    /**
     * Call the API to send out the download reports for the specified month 
     * and year
     * 
     * @param month A month (0-11)
     * @param year A year
     */
    private void processEmails(int month, int year) {
        WebResource webResource = client.resource(props.getProperty("api_url") + "/reporting/monthlyDownload?month=" + month + "&year=" + year);
        WebResource.Builder builder = webResource.getRequestBuilder();
        builder = builder.cookie(authCookie);       
        
        System.out.println("API Sending Emails....");

        ClientResponse response = builder.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("FAILURE : API email service poking failed with code : " + response.getStatus() + "\n     " + response.getEntity(String.class));
        }

        System.out.println("API Sent Emails....");
        System.out.println("Monthly emails sent successfully");
        
        // Log out
        webResource = client.resource(props.getProperty("api_url") + "/user/logout");
        webResource.cookie(authCookie);
        System.out.println("Logging out....");
        response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        
        if (response.getStatus() != 200) {
            throw new RuntimeException("WARNING: Could not log out of API service");
        } else {
            System.out.println("Successfully logged out and completed monthly batch email");
        }
        
        System.exit(0);
    }

    /**
     * Setup the mailer service and get it to poke the monthly reports
     * generation service
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReportMailer mailer = new ReportMailer();
        try {
            if (args.length == 0) {
                mailer.pokeEmailService();
            } else if (args.length == 1) {
                Integer month = Integer.parseInt(args[0]);
                if (month >= 1 && month <= 12) {
                    mailer.pokeEmailService(month);
                } else {
                    throw new RuntimeException("Month is not in a valid range (1-12)");
                }
            } else if (args.length == 2) {
                Integer month = Integer.parseInt(args[0]);
                Integer year = Integer.parseInt(args[1]);
                if (month >= 1 && month <= 12) {
                    mailer.pokeEmailService(month, year);
                } else {
                    throw new RuntimeException("Month is not in a valid range (1-12)");
                }
            } else { 
                throw new RuntimeException("Usage x.jar month(1-12)[Optional] year[Optional]");
            }
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Usage x.jar month(1-12)[Optional] year[Optional]");
        }
    }
}