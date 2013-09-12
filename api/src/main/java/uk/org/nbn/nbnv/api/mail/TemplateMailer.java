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
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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
 * @author Christopher Johnson
 */
@Component
public class TemplateMailer {

    @Autowired MailSender mailSender;
    @Autowired Properties properties;
    private static Log logger = LogFactory.getLog(TemplateMailer.class);
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

        if ("dev".equals(properties.getProperty("email_mode"))) {
            logger.info("Mail Sent -> <" + to + "> Subject: <" + subject + "> Email Template: " + template);
        } else {
            mailSender.send(test);
        }
    }

    public void sendMime(
            final String template,
            final String to,
            final String subject,
            final Map<String, Object> data) throws IOException, TemplateException, MessagingException {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(properties.getProperty("email_host"));
        sender.setPort(Integer.parseInt(properties.getProperty("email_port")));
        
        MimeMessage mm = sender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mm);
        message.setTo(to);
        message.setFrom(properties.getProperty("email_from"));
        message.setSubject(subject);
        message.setText(FreeMarkerTemplateUtils.processTemplateIntoString(
                configuration.getTemplate(template), data), true);

        if ("dev".equals(properties.getProperty("email_mode"))) {
            logger.info("Mail Sent -> <" + to + "> Subject: <" + subject + "> Email Template: " + template);
        } else {
            sender.send(mm);
        }
    }
}
