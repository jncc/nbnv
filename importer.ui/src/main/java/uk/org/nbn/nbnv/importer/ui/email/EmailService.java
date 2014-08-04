package uk.org.nbn.nbnv.importer.ui.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author Stephen Batty
 *         Date: 01/08/14
 *         Time: 16:15
 *         <p/>
 */
@Service
public class EmailService {

    private static Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value(value = "email_port")
    private String emailPort;
    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendLog(String to, File log) {
        logger.info("creating a log email message");
        String messageText = "Stage one import validation complete, you can view results and any errors if present in attached log file";
        String subject = "Validation of data complete, see attached log file";
        sendFile(to, subject, messageText, log);
    }

    private void sendFile(String to, String subject, String messageText, File file) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        //FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));
        try {
            // use the true flag to indicate you need a multipart message
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(messageText);
            helper.addAttachment(file.getName(), file);
        } catch (MessagingException e) {
            // log message
            // and send a different email saying its screwed up to admin
            logger.error("failed to create email message");
        }
        mailSender.send(message);
        logger.info("email sent successfully to "+to+" with file named : "+file.getName());
    }
}
