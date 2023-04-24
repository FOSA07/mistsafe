package mist_safe.mistsafe.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mist_safe.mistsafe.Services.AuthenticationService;

@Controller
public class MainController {
    
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

    @PostMapping("/verifyemail")
    public String openEmailSent(@RequestParam String email, Model model){
        AuthenticationService authService = new AuthenticationService();
        // authService.registerWithEmailAndPassword(email, );

        model.addAttribute("email", email);
        return "emailsent";
    }
}
