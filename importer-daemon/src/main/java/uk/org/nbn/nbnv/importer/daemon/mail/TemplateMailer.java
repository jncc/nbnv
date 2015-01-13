/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.daemon.mail;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * The following bean enables emailing of freemarker templates from the address
 * specified in the api.properties
 *
 * Templates are loaded relative to this class file
 *
 * @author Matt Debont
 */
public class TemplateMailer {
    
    private final Configuration configuration;
    private final Properties properties;
    //private final Properties props;
    
    private final String host;
    private final int port;
    
    private static final Logger logger = Logger.getLogger(TemplateMailer.class);

    public TemplateMailer(Properties properties) throws IOException {
        this.properties = properties;
        
        host = properties.getProperty("email_host");
        port = Integer.parseInt(properties.getProperty("email_port"));  
        
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(TemplateMailer.class, "");
    }
    
    public void sendMime(
            final String template,
            final String to,
            final String subject,
            final Map<String, Object> data) throws IOException, TemplateException, MessagingException {     
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        
        //Session session = Session.getInstance(props);
               
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom(properties.getProperty("email_from"));
        helper.setSubject(subject);
        
        helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(
                configuration.getTemplate(template), data), true);
        
        
        if (data.containsKey("attachment")) {
            FileSystemResource file = new FileSystemResource((File) data.get("attachment"));
            helper.addAttachment("output.zip", file);
        }

        if ("dev".equals(properties.getProperty("email_mode"))) {
            logger.info("DEV MODE: Mail Sent -> <" + to + "> Subject: <" + subject + "> Email Template: " + template);
        } else {
            mailSender.send(message);
            logger.info("Mail Sent -> <" + to + "> Subject: <" + subject + "> Email Template: " + template);
        }
    }
}
