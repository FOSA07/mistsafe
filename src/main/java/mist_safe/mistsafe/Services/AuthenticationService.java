package mist_safe.mistsafe.Services;

import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
// import javax.mail.Authenticator;
// import javax.mail.Message;
// import javax.mail.MessagingException;
// import javax.mail.PasswordAuthentication;
// import javax.mail.Session;
// import javax.mail.Transport;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;
// import javax.mail.util.ByteArrayDataSource;
// import javax.activation.DataContentHandler;
// import javax.activation.DataHandler; 


@Service
public class AuthenticationService {


    public Map<String, Object> registerWithEmailAndPassword(
      String email, String password) {

        ActionCodeSettings actionCodeSettings = 
          ActionCodeSettings.builder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                .setUrl("https://mistsafe-production.up.railway.app/")
                // This must be true
                .setHandleCodeInApp(true)
                .build();

          CreateRequest request = new CreateRequest()
              .setEmail(email)
              .setEmailVerified(false)
              .setPassword(password)
              // .setPhoneNumber("+11234567890")
              // .setDisplayName("John Doe")
              // .setPhotoUrl("http://www.example.com/12345678/photo.png")
              .setDisabled(false);

          UserRecord userRecord;
          try {
            userRecord = FirebaseAuth.getInstance().createUser(request);

            System.out.println("Successfully created new user: " + userRecord.getUid());

            String link = FirebaseAuth.getInstance().generateEmailVerificationLink(
                email, actionCodeSettings);
            // Construct email verification template, embed the link and send
            // using custom SMTP server.

            String name = "username";
            // sendCustomEmail(email, name, link);

            String subject = "Verify your email address";
            String text = "Hi " + name + ",\n\nPlease click on the following link to verify your email address:\n\n" + link + "\n\nBest regards,\nThe MyWebsite Team";
            
            sendEmailVerificationLink(email, link);
            // sendEmail(email,subject, text );

            System.out.println("finished");
          } catch (FirebaseAuthException | MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
          }
          
                
        return null;
      }


        public static void sendEmailVerificationLink(String email, String link) throws AddressException, MessagingException {
    // Use your favorite email service provider to send the email
    // Here's an example using JavaMail API
    // Properties props = new Properties();
    // props.put("mail.smtp.auth", "true");
    // props.put("mail.smtp.starttls.enable", "true");
    // props.put("mail.smtp.host", "smtp.gmail.com");
    // props.put("mail.smtp.port", "587");

    // Session session = Session.getInstance(props,
    //   new javax.mail.Authenticator() {
    //     protected PasswordAuthentication getPasswordAuthentication() {
    //         return new PasswordAuthentication("faletioluwaseyisam@gmail.com", "gyxjhrbqvspriejk");
    //     }
    //   });

    // try {
    //     Message message = new MimeMessage(session);
    //     message.setFrom(new InternetAddress("faletioluwaseyisam@gmail.com"));
    //     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
    //     message.setSubject("Email Verification Link");
    //     String body = "Please click on the following link to verify your email address:\n\n" + link;
    //     message.setText(body);
    //     message.setDataHandler(new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain")));
    //     message.setHeader("Content-Type", "text/plain; charset=UTF-8");

    //     // String body = "Please click on the following link to verify your email address:\n\n" + link;
    //     Transport.send(message);

    //     System.out.println("Email sent successfully.");
    // } catch (MessagingException e) {
    //     throw new RuntimeException(e);
    // }25

    Properties prop = new Properties();
    prop.put("mail.smtp.auth", true);
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.host", "smtp.mailtrap.io");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
    prop.put("mail.username", "faletioluwaseyisam@gmail.com");
    prop.put("mail.password", "gyxjhrbqvspriejk");

    Session session = Session.getInstance(prop, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("faletioluwaseyisam@gmail.com", "gyxjhrbqvspriejk");
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress("faletioluwaseyisam@gmail.com"));
    message.setRecipients(
      Message.RecipientType.TO, InternetAddress.parse("olusinafaleti@gmail.com"));
    message.setSubject("Mail Subject");

    String msg = "This is my first email using JavaMailer";

    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(mimeBodyPart);

    message.setContent(multipart);

    Transport.send(message);
  }
}
