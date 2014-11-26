/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.daemon.mail;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * The following bean enables emailing of freemarker templates from the address
 * specified in the api.properties
 *
 * Templates are loaded relative to this class file
 *
 * @author Matt Debont
 */
@Component
public class TemplateMailer {
    
    private final Configuration configuration;
    
    private static final Logger logger = LoggerFactory.getLogger(TemplateMailer.class);
    @Autowired private Properties properties;
    @Autowired private JavaMailSenderImpl mailSender;

    public TemplateMailer() throws IOException {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(TemplateMailer.class, "");
    }
    
    public void sendMime(
            final String template,
            final String to,
            final String subject,
            final Map<String, Object> data) throws IOException, TemplateException, MessagingException {               
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mm);
        message.setTo(to);
        message.setFrom(properties.getProperty("email_from"));
        message.setSubject(subject);
        message.setText(FreeMarkerTemplateUtils.processTemplateIntoString(
                configuration.getTemplate(template), data), true);
        
        if (data.containsKey("attachment")) {
            FileSystemResource file = new FileSystemResource((String) data.get("attachment"));
            message.addAttachment("Output.txt", file);
        }

        if ("dev".equals(properties.getProperty("email_mode"))) {
            logger.info("Mail Sent -> <" + to + "> Subject: <" + subject + "> Email Template: " + template);
        } else {
            mailSender.send(mm);
        }
    }
}
