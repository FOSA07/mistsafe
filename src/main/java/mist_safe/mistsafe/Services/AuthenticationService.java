package mist_safe.mistsafe.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class AuthenticationService {
    
    // private final FirebaseAuth _auth = FirebaseAuth.getInstance();

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

            sendEmail(email,subject, text );
          } catch (FirebaseAuthException | MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
                
        return null;
      }

    private void sendCustomEmail(String email, String name, String link) {

    }

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom("mistSafe");
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
    }
}
