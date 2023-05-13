package mist_safe.mistsafe.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.*;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {
///https://bootstrapmade.com/
    @Autowired
    private JavaMailSender mailSender;
    public Map<String, Object> registerWithEmailAndPassword(
      String email, String password) {

        ActionCodeSettings actionCodeSettings = 
          ActionCodeSettings.builder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                .setUrl("https://mistsafe-production.up.railway.app/home?email=" + email+"&status=verifyEmail")
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
            
            // String token = userRecord.
            String link = FirebaseAuth.getInstance().generateEmailVerificationLink(
                email, actionCodeSettings);
            // Construct email verification template, embed the link and send
            // using custom SMTP server.

            String name = "username";
            // sendCustomEmail(email, name, link);

                        
            System.out.println("finished");

            Map reply = new HashMap<String, Object>();
            reply.put("status", "success");
            reply.put("link", link);

            return reply;
          } catch (FirebaseAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            Map reply = new HashMap<String, Object>();
            reply.put("status", "error");
            reply.put("link", "");
          }
          
                
        return null;
      }

      public Map<String, Object> signInWithEmailAndPassworde(String email, String password, HttpSession session){
        try{
          // @Autowired
          FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

          UserRecord userRecord = firebaseAuth.getUserByEmail(email);
          Map<String, Object> user = new HashMap<String, Object>();
          user.put("userId", userRecord.getUid());
          user.put("userEmail", userRecord.getEmail());
          if (userRecord.isDisabled()) {
            // If the user is disabled, return 401 Unauthorized
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
          }
          // Set the user in the session
          session.setAttribute("user", user);
          // Set the expiry time of the session to 30 minutes
          session.setMaxInactiveInterval(30 * 60);
          // Return the signed-in user
          return user;
          // return ResponseEntity.ok(user);
        }catch(Exception e){

        }
        return null;
      }

      private static final long COOKIE_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(30);

    public String login(String email, String password,
            HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {

          FirebaseAuth auth = FirebaseAuth.getInstance();
          
        UserRecord userRecord = auth.getUserByEmail(email);
        
        
        if (!userRecord.isEmailVerified()) {
            // handle unverified email error
            return "email-not-verified";
        }

        // create custom token
        String customToken = auth.createCustomToken(userRecord.getUid());

        // set the session cookie
        String sessionCookie = auth.createSessionCookie(customToken, getSessionCookieOptions());
        request.getSession().setAttribute("sessionCookie", sessionCookie);

        // set the idToken cookie
        Cookie idTokenCookie = new Cookie("idToken", sessionCookie);
        idTokenCookie.setHttpOnly(true); // make the cookie accessible only through HTTP(S)
        idTokenCookie.setSecure(true); // make the cookie accessible only over HTTPS
        idTokenCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(30)); // set the cookie expiration time to 30 days
        idTokenCookie.setPath("/"); // set the cookie path to "/"
        response.addCookie(idTokenCookie); // add the cookie to the response


            // redirect the user to the home page
            return "home";
        } catch (FirebaseAuthException e) {
            // handle authentication error
            if (e.getErrorCode().equals("user-not-found")) {
              return "user-not-found";
              // handle user not found error
            } else if (e.getErrorCode().equals("invalid-password")) {
              // handle invalid password error
            } else {
              // handle other authentication errors
            }
            return "error";
        } catch (Exception e) {
            // handle other exceptions
            return "unknown-error";
        }
    }

      private static SessionCookieOptions getSessionCookieOptions() {
        return SessionCookieOptions.builder()
                .setExpiresIn(COOKIE_EXPIRATION_TIME)
                .build();
    }

    public boolean resendVerificationLink(String email){

      try{
        EmailService emailService = new EmailService();

        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

        String uid = userRecord.getUid();

        ActionCodeSettings actionCodeSettings = 
          ActionCodeSettings.builder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                .setUrl("https://mistsafe-production.up.railway.app/home?email=" + email+"&status=verifyEmail")
                // This must be true
                .setHandleCodeInApp(true)
                .build();

        String link = FirebaseAuth.getInstance().generateEmailVerificationLink(
                email, actionCodeSettings);

        String subject = "Verify your email address";
        String text = "Hi " + email + ",\n\nPlease click on the following link to verify your email address:\n\n" + link + "\n\nBest regards,\nThe mistSafe Team";

        String result = emailService.sendEmail(email, subject, text);

        if(result == "fail"){
          return false;
        }
        return true;
      }catch (Exception e){
        return false;
      }
    }
    }