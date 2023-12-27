package com.eldhimni.controller;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.PW;
import com.eldhimni.entity.Professor;
import com.eldhimni.entity.Student;
import com.eldhimni.repository.GroupeRepo;
import com.eldhimni.repository.PWRepo;
import com.eldhimni.repository.ProfessorRepo;
import com.eldhimni.repository.StudentRepo;


@Controller
public class GroupeController {

    @Autowired
    private GroupeRepo groupeRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
	private PWRepo pwRepository;
    
    @Autowired
    private ProfessorRepo professorRepo;
    @GetMapping("/home")
    public String showIndex(Model model, Principal principal) {
    	 if (principal == null) {
    	        // Gérer le cas où le principal est null, par exemple, rediriger vers la page de connexion
    	        return "redirect:/login";
    	    }

        // Récupérer le nom du professeur à partir du Principal
        String professorEmail = principal.getName();

        // Trouver le professeur par son email
        Professor professor = professorRepo.findByEmail(professorEmail);

        // Récupérer les groupes associés au professeur
        List<Groupe> professorGroupes = professor.getGroupes();
System.out.println(professorGroupes);
        model.addAttribute("groupes", professorGroupes);
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("mode", "");
        
        // Ajoutez le professeur connecté au modèle
        model.addAttribute("professor", professor);

        return "home";
    }

    @GetMapping("/groupe/pws/{id}")
    public String showPws(@PathVariable("id") long id, Model model, Principal principal) {
        String email = principal.getName();
        Professor professor = professorRepo.findByEmail(email);
        model.addAttribute("professor", professor);

        Groupe groupe = groupeRepo.findById(id).orElse(null);
        Set<PW> pws = pwRepository.findByGroupes(groupe);

        // Assuming you have a method in your repository to retrieve students by Groupe
        Set<Student> students = studentRepo.findByGroupe(groupe);

        model.addAttribute("pws", pws);
        model.addAttribute("students", students);
        return "groupedetails";
    }

     
    @GetMapping("/groupe/add")
    public String showAddForm(Model model) {
        model.addAttribute("groupes", groupeRepo.findAll());
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("mode", "add");
        return "home";
    }

    
    @PostMapping("/groupe/addgroupe")
    public String addGroupe(@Validated Groupe groupe, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            return "home";
        }

        // Récupérer le nom du professeur à partir du Principal
        String professorEmail = principal.getName();

        // Trouver le professeur par son email
        Professor professor = professorRepo.findByEmail(professorEmail);

        // Associer le groupe au professeur
        groupe.setProfessor(professor);

        // Enregistrez le groupe dans la base de données
        groupeRepo.save(groupe);

        return "redirect:/home";
    }

       
       

    @GetMapping("/groupe/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Groupe groupe = groupeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupe Id:" + id));
        model.addAttribute("groupes", groupeRepo.findAll());
        model.addAttribute("groupe", groupe);
        model.addAttribute("mode", "update");
        return "home";
    }
    @PostMapping("/groupe/update/{id}")
    public String updateGroupe(@PathVariable("id") long id, @Validated Groupe groupe, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            groupe.setId(id);
            return "home";
        }

        // Récupérer le nom du professeur à partir du Principal
        String professorEmail = principal.getName();

        // Trouver le professeur par son email
        Professor professor = professorRepo.findByEmail(professorEmail);

        // Associer le groupe au professeur
        groupe.setProfessor(professor);

        groupeRepo.save(groupe);
        return "redirect:/home";
    }


    @GetMapping("/groupe/delete/{id}")
    public String deleteGroupe(@PathVariable("id") long id, Model model) {
        Groupe groupe = groupeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupe Id:" + id));
        groupeRepo.delete(groupe);
        return "redirect:/home";
        }}