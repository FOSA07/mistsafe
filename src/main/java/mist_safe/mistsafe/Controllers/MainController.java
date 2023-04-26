package mist_safe.mistsafe.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import mist_safe.mistsafe.Services.AuthenticationService;
import mist_safe.mistsafe.Services.EmailService;

@Controller
public class MainController {

    private final AuthenticationService authService;

    public MainController(AuthenticationService authService) {
        this.authService = authService;
    }
    
    @GetMapping("/")
    public String openIndex() {
        return "index";
    }

    @GetMapping("/signup")
    public String openSignUp(){
        return "signup";
    }

    @GetMapping("/signin")
    public String openSignIn(){
        return "signin";
    }

    @GetMapping("/error-page")
    public String openErrorPage(){
        return "errorpage";
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("/verifyemail")
    public String openEmailSent(@RequestParam String email, String password, Model model) throws AddressException, MessagingException{

        try{
            Map reply = authService.registerWithEmailAndPassword(email, password);
            // authService.sendEmailVerificationLink("name","link");
        
            if(reply.get("status")=="error"){
                return "errorpage";
            }

            String subject = "Verify your email address";
            String link = String.valueOf( reply.get("link") );
            String text = "Hi " + email + ",\n\nPlease click on the following link to verify your email address:\n\n" + link + "\n\nBest regards,\nThe MyWebsite Team";

            String result = emailService.sendEmail(email, subject, text);
            model.addAttribute("email", email);
            

            if(result == "success"){
                return "emailsent";
            }else{
                return "errorpage";
            }
        }catch (Exception e){
            return "errorpage";
        }
    }

}
