/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.resetter;

import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Matt Debont
 */
public class Resetter {
    private final ResetUserDatasetAccess userAccessReset;
    private final ResetOrganisationDatasetAccess organisationAccessReset;
    
    public Resetter() {
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
            System.out.println("Resetting Access to " + args[0]);
            Resetter reset = new Resetter();
            System.out.println("Resetting Organisation Access to " + args[0]);
            reset.doOrganisationAccessReset(args[0]);
            System.out.println("Resetting User Access to " + args[0]);
            reset.doUserAccessReset(args[0]);
            System.out.println("Finished Access Resets for " + args[0]);
        } else {
            System.out.println("NBNV Dataset Access Reset Tool - Usage:\n" +
                    "  java -jar NBNV-AccessResetter.jar DatasetKey\n" +
                    "    DatasetKey    The key of the dataset to reset access for i.e. GA000466"
            );
            System.exit(-1);
        }
        
    }
}
