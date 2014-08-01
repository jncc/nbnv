package uk.org.nbn.nbnv.importer.ui.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Stephen Batty
 *         Date: 01/08/14
 *         Time: 16:15
 *
 *  http://jodd.org/doc/email.html
 */
@Service
public class EmailService {
     @Value(value = "email_host")
     private String emailHost;

    // send email with log attached
    public boolean sendLogs(){
        return true;
    }
    public void example(){
    SmtpServer smtpServer =
            new SmtpServer("mail.jodd.org", new SimpleAuthenticator("user", "pass"));
    ...
    SendMailSession session = smtpServer.createSession();
    session.open();
    session.sendMail(email1);
    session.sendMail(email2);
    session.close();
    }
}
