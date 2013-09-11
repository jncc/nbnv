/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.reportmailer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Matt Debont
 */
public class ReportMailer {
    
    private Client client;
    private Properties props;
    
    public ReportMailer() {
        props = new Properties();
        try {
            props.load(new FileInputStream(new File("settings.properties")));
        } catch (FileNotFoundException ex) {
            System.err.println("Could not find properties file");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Unknown error occured");
            System.exit(1);
        }

        client = Client.create();
    }
    
    public void pokeEmailService() {
        System.out.println("Poking API....");
        WebResource webResource = client.resource(props.getProperty("api") + "/reports/");
        System.out.println("API Sending Emails....");
        ClientResponse response = webResource.accept("application/json")
                   .get(ClientResponse.class);
        
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : API email service poking failed with code : " + response.getStatus() + "\n     " + response.getEntity(String.class));
        }
        
        System.out.println("API Sent Emails....");
        System.out.println("Monthly emails sent successfully");
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
        mailer.pokeEmailService();
    }
}