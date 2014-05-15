/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.rest.resources.OrganisationAccessRequestResource;
import uk.org.nbn.nbnv.api.rest.resources.UserAccessRequestResource;

/**
 *
 * @author Matt Debont
 */
public class RevokeAccessService {
    
    @Autowired private UserAccessRequestResource userResource;
    @Autowired private OrganisationAccessRequestResource orgResource;
    @Autowired private UserMapper userMapper;
    @Autowired private Properties properties;
    @Autowired private TemplateMailer mailer;
    
    private Logger logger = LoggerFactory.getLogger(RevokeAccessService.class);
    
    @Scheduled(cron = "0 0 0 * * *")
    public void revokeNightly() {
        revokeAccess();
    }

    public void revokeAccess() {
        logger.info("Starting Access Request Revoke Task");
        User admin = userMapper.getUser(properties.getProperty("sysadmin_name"));
        Response userResp = userResource.sysAdminRevoke(admin, Boolean.parseBoolean(properties.getProperty("silent_revoke")));
        Response orgResp = orgResource.sysAdminRevoke(admin, Boolean.parseBoolean(properties.getProperty("silent_revoke")));
        
        if (userResp.getStatus() != 200 || orgResp.getStatus() != 200) {
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("taskname", "Revoke Expired Access Requests");
            message.put("failTime", new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()));
            message.put("failMsg", "User Access Response:\n" + userResp.getEntity() + "\nOrg Access Response:\n" + orgResp.getEntity());
            try {
                mailer.send("taskFailure.ftl", properties.getProperty("admin_email"), "Expired Access Revoke Task Failed", message);
                logger.error("Failed to call revoke access, sent warning email to " + properties.getProperty("admin_email"));
            } catch (Exception ex) {
                logger.error("WARNING: TASK FAILED AND DID NOT SEND NOTIFICATION! " + message.get("taskname") + "\n" + message.get("failMsg"));
            }
        }
        logger.info("Completed Access Request Revoke Task");
    }
}
