package com.eldhimni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eldhimni.entity.Tooth;
import com.eldhimni.entity.Tooth;
import com.eldhimni.entity.User;
import com.eldhimni.repository.ToothRepo;
import com.eldhimni.repository.ToothRepo;
import com.eldhimni.repository.UserRepo;
import com.eldhimni.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class SninaController {

    @Autowired
    UserRepo utilisateurRepository;
    @Autowired
    ToothRepo toothRepo;
    

    @Autowired
	private UserService userService;

    @GetMapping({"/snina"})
    public String showIndexx(Model model){
        model.addAttribute("users",toothRepo.findAll());
        model.addAttribute("user",new Tooth());
        model.addAttribute("mode","");
        return "snina";
    }

    @GetMapping("/snina/adduser")
    public String showAddUserForm(Model model){
        model.addAttribute("users",toothRepo.findAll());
        model.addAttribute("user",new Tooth());
        model.addAttribute("mode","add");
        return "snina";
    }
    @PostMapping("/snina/addUtilisateur")
    public String adduser(@Validated Tooth user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // If there are validation errors, return to the form with error messages
            return "user";
        }
        
        // Print the user object to check if it's received correctly
        System.out.println(user);

		Tooth u = toothRepo.save(user);
        return "redirect:/snina";
    }


    @GetMapping("/snina/editUser/{id}")
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {
    	Tooth tooth = toothRepo.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("users",toothRepo.findAll());
        model.addAttribute("user", tooth);
        model.addAttribute("mode","update");
        return"snina";
    }

    @PostMapping("/snina/updateUser/{id}")
    public String updateuser(@PathVariable("id") Long id, @Validated Tooth user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return"user";
        }
		Tooth u = toothRepo.save(user);
        return "redirect:/snina";
    }

    @GetMapping("/snina/deleteUser/{id}")
    public String deleteuser(@PathVariable("id") long id, Model model) {
    	Tooth tooth = toothRepo.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Invalid user Id:" + id));
        toothRepo.delete(tooth);
        return "redirect:/snina";}}
