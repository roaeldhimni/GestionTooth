package com.eldhimni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.Professor;
import com.eldhimni.entity.User;
import com.eldhimni.repository.ProfessorRepo;
import com.eldhimni.repository.UserRepo;
import com.eldhimni.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    UserRepo utilisateurRepository;
    @Autowired
    ProfessorRepo profRepo;
    

    @Autowired
	private UserService userService;

    @GetMapping("/students")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = (List<User>) utilisateurRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
        }
    
    @GetMapping({"/admin"})
    public String showIndexx(Model model){
        model.addAttribute("users",profRepo.findAll());
        model.addAttribute("user",new Professor());
        model.addAttribute("mode","");
        return "user";
    }

    @GetMapping("/admin/adduser")
    public String showAddUserForm(Model model){
        model.addAttribute("users",utilisateurRepository.findAll());
        model.addAttribute("user",new Professor());
        model.addAttribute("mode","add");
        return "user";
    }
    @PostMapping("/admin/addUtilisateur")
    public String adduser(@Validated Professor user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // If there are validation errors, return to the form with error messages
            return "user";
        }
        
        // Print the user object to check if it's received correctly
        System.out.println(user);

		User u = userService.saveUser(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/editUser/{id}")
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {
    	User user = utilisateurRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("users",utilisateurRepository.findAll());
        model.addAttribute("user", user);
        model.addAttribute("mode","update");
        return"user";
    }

    @PostMapping("/admin/updateUser/{id}")
    public String updateuser(@PathVariable("id") Long id, @Validated Professor user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return"user";
        }
		User u = userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/deleteUser/{id}")
    public String deleteuser(@PathVariable long id) {
        Optional<Professor> professorOptional = profRepo.findById(id);

        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();

            // Retirer l'enseignant de tous les groupes associés
            for (Groupe groupe : professor.getGroupes()) {
                groupe.setProfessor(null);
            }

            // Supprimer l'enseignant
            profRepo.delete(professor);
        }
         return "redirect:/admin";}
    
  

}
   