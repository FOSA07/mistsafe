package mist_safe.mistsafe.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mist_safe.mistsafe.Modal.ResendEmailResponse;
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

    @GetMapping("/signin/emailnotverified")
    public String openEmailNotVerified(){
        return "emailnotverified";
    }

    @GetMapping("/emailsent")
    public String openEmailSent(HttpServletRequest request, Model model){
        String email = request.getParameter("email");

        model.addAttribute("email", email);
        return "emailsent";
    }

    @GetMapping("/home")
    public String openHome(){
        return "home";
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
            String text = "Hi " + email + ",\n\nPlease click on the following link to verify your email address:\n\n" + link + "\n\nBest regards,\nThe mistSafe Team";

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

    @PostMapping("/homeservice")
    public String getHome(@RequestParam String email, Model model, String status,
                      @RequestParam(required = false) 
                      HttpSession session, 
                      HttpServletRequest request, 
                      HttpServletResponse response) {
        if (email == null || email == "") {
            // user is not logged in, redirect to login/signup page
            return "redirect:/signin";
        }else if (status == "verifyEmail"){
            String result = authService.login(email, status, session, request, response);

            if (result == "home"){
                // user is logged in, display home page
                model.addAttribute("email", email);
                return "home";
            }else if(result == "email-not-verified"){
                model.addAttribute("email", email);
                return "redirect:/signin/emailnotverified";
            }

            model.addAttribute("result", result);
            return "redirect:/errorpage";
            
        }
        return "redirect:/signin";
 
    }

    @PostMapping("/resend-email-verification")
    @ResponseBody
    public ResponseEntity<ResendEmailResponse> resendEmailVerification(@RequestBody String email) {
        // Call your email service to resend the verification email
        boolean success = authService.resendVerificationLink(email);
        
        if (success) {
            ResendEmailResponse response = new ResendEmailResponse("success", email);
            return ResponseEntity.ok(response);
        } else {
            ResendEmailResponse response = new ResendEmailResponse("error", email);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // @PostMapping("/login")
    // public String login(@RequestParam String email, @RequestParam String password) {
    //     try {
    //         String result = authService.login(email, password, null, null, null);
    //     } catch (FirebaseAuthException e) {
    //         // handle authentication error
    //         return "login";
    //     }
    // }


}
