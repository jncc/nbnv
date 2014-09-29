/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.reseter;

import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Matt Debont
 */
public class Reseter {
    private final ResetUserDatasetAccess userAccessReset;
    private final ResetOrganisationDatasetAccess organisationAccessReset;
    
    public Reseter() {
        ClassPathXmlApplicationContext ctx = 
                new ClassPathXmlApplicationContext("applicationContext.xml");
        this.userAccessReset = ctx.getBean(ResetUserDatasetAccess.class);
        this.organisationAccessReset = ctx.getBean(ResetOrganisationDatasetAccess.class);
    }
    
    public boolean doUserAccessReset(String dataset) throws IOException {
        return userAccessReset.resetAllAccess(dataset);
    }
    
    public boolean doOrganisationAccessReset(String dataset) throws IOException {
        return organisationAccessReset.resetAllAccess(dataset);
    }
    
    public static void main (String[] args) throws IOException {
        if (args.length == 1) {
            Reseter reset = new Reseter();
            reset.doOrganisationAccessReset(args[0]);
            reset.doUserAccessReset(args[0]);
        } else {
            System.exit(-1);
        }
        
    }
}
