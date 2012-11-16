/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.mail;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * The following bean enables emailing of freemarker templates from the address 
 * specified in the api.properties
 * 
 * Templates are loaded relative to this class file
 * @author Christopher Johnson
 */
@Component
public class TemplateMailer {
    
    @Autowired MailSender mailSender;
    @Autowired Properties properties;
    
    private Configuration configuration;
    
    public TemplateMailer() {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(TemplateMailer.class, "");
    }
    
    public void send(String template, String to, String subject, Map<String, Object> data) throws IOException, TemplateException {
        SimpleMailMessage test = new SimpleMailMessage();
        test.setTo(to);
        test.setSubject(subject);
        test.setFrom(properties.getProperty("email_from"));
        test.setText(FreeMarkerTemplateUtils.processTemplateIntoString(
                configuration.getTemplate(template), data));
        
        mailSender.send(test);
    }
}
