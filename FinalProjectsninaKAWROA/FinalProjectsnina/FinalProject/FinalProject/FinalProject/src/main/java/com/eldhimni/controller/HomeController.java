package com.eldhimni.controller;

import java.security.Principal;
import java.util.Random;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eldhimni.entity.User;
import com.eldhimni.repository.UserRepo;
import com.eldhimni.service.UserService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo userRepo;
	  @Autowired
	 private JavaMailSender javaMailSender;


	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("userprofile", user);
		}

	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
//	@GetMapping("/admin")
//	public String admin() {
//		return "admin";
//	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/user/profile")
	public String profile(Principal p, Model m) {
		String email = p.getName();
		User user = userRepo.findByEmail(email);
		m.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/user/home")
	public String home() {
		return "home";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session, Model m) {

		// System.out.println(user);

		User u = userService.saveUser(user);

		if (u != null) {
			// System.out.println("save sucess");
			session.setAttribute("msg", "Register successfully");

		} else {
			// System.out.println("error in server");
			session.setAttribute("msg", "Something wrong server");
		}
		return "redirect:/register";
	}
	
	@GetMapping("/forgotPassword")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "forgotpassword";
    }
    @Transactional
    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model,HttpSession session) {
        User User = userRepo.findByEmail(email);
        System.out.println(User);
        if (User != null) {
            String verificationCode = generateVerificationCode();


            // Envoyer le code de vérification par e-mail
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Réinitialisation du mot de passe");
            message.setText("Votre code de vérification est : " + verificationCode);
            javaMailSender.send(message);
            session.setAttribute("verificationCode", verificationCode);
            session.setAttribute("email", email);



            // Ajoutez la redirection vers la page reset-password ici
            return "redirect:/resetpassword";
        } else {
            model.addAttribute("error", "Aucun User trouvé avec cet e-mail.");
        }

        return "redirect:/resetpassword";}
  private String generateVerificationCode() {
        // Générez un code de vérification unique (par exemple, un code aléatoire)
        String verificationCode = generateRandomCode();

        // Vous pouvez définir la date d'expiration ici si nécessaire

        return verificationCode;
    }

    // ... (other methods)

    private String generateRandomCode() {
        // Ajoutez ici la logique pour générer un code aléatoire (par exemple, une chaîne de caractères aléatoires)
        // Par exemple, utilisez la classe Random pour générer un code aléatoire de longueur fixe
        int codeLength = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            code.append(randomChar);
        }

        return code.toString();}
    
    @GetMapping("/resetpassword")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "resetpassword";
    }

    @PostMapping("/resetpassword")
    public String resetPassword(@ModelAttribute("user") User User,@RequestParam("verificationCode") String userEnteredCode,HttpSession session,BindingResult bindingResult, Model model) {
        // Récupérer le code de vérification de la session
    	String verificationCode = (String) session.getAttribute("verificationCode");
        String email = (String) session.getAttribute("email");

        System.out.println("Email attribute in resetPassword: " + email);

        System.out.println("ver"+verificationCode);
        System.out.println("usercode"+userEnteredCode);


        if (verificationCode != null && verificationCode.equals(userEnteredCode)) {

            session.setAttribute("email", email);
            return "newpassword";

        } else {
            // Le code de vérification est incorrect, affichez un message d'erreur
            model.addAttribute("error", "Code de vérification incorrect.");
            return "resetpassword";
        }
    }
    @GetMapping("/newpassword")
    public String showNewPasswordForm(Model model, HttpSession session) {
        // Récupérez l'e-mail de la session
        String email = (String) session.getAttribute("email");

        // Vérifiez si l'e-mail est présent dans la session
        if (email != null) {
            System.out.println("Email attribute found in the session: " + email);
        } else {
            System.out.println("Email attribute not found in the session");
            // Gérez le cas où l'e-mail n'est pas trouvé dans la session
        }

        // Passez l'e-mail à la page
        model.addAttribute("email", email);
        return "newpassword";
    }


    @PostMapping("/newpassword")
    public String updatePassword(@RequestParam("newPassword") String newPassword,Model model,HttpSession session) {{
        // Récupérer l'User par e-mail
        String email = (String) session.getAttribute("email");
        System.out.println(email);

        User loggedInUserEmail = userRepo.findByEmail(email);
        System.out.println(loggedInUserEmail);

        if (loggedInUserEmail != null) {
            // Mettre à jour le mot de passe de l'User avec le nouveau mot de passe
            loggedInUserEmail.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
            userRepo.save(loggedInUserEmail);

            // Rediriger vers une page de succès ou une autre page appropriée
            return "redirect:/signin";
        } else {
            // L'User n'a pas été trouvé, gérer l'erreur
            model.addAttribute("error", "User non trouvé");
            return "newpassword";}}}}
    
