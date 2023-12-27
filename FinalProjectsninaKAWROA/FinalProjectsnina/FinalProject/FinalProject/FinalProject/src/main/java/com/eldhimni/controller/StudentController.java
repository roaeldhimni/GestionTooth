package com.eldhimni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.Professor;
import com.eldhimni.entity.Student;
import com.eldhimni.entity.StudentPW;
import com.eldhimni.entity.Student;
import com.eldhimni.entity.User;
import com.eldhimni.repository.GroupeRepo;
import com.eldhimni.repository.ProfessorRepo;
import com.eldhimni.repository.StudentPWRepo;
import com.eldhimni.repository.StudentRepo;
import com.eldhimni.repository.UserRepo;
import com.eldhimni.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class StudentController {

    @Autowired
    StudentRepo studentRepo;
    @Autowired
    private GroupeRepo groupeRepo;
    
    @Autowired
    private ProfessorRepo profRepo;
    @Autowired
    private StudentPWRepo studentpwRepo;
    
    @Autowired
	private UserService userService;
    private String encodePhoto(byte[] photo) {
        return (photo != null && photo.length > 0) ? Base64.getEncoder().encodeToString(photo):"";}
    
    
    
    @GetMapping("/student")
    public String showIndexx(Model model, Principal principal) {
        if (principal == null) {
            // Handle the case where the principal is null, e.g., redirect to the login page
            return "redirect:/login";
        }
        

        // Récupérer le nom du professeur à partir du Principal
        String professorEmail = principal.getName();

        // Trouver le professeur par son email
        Professor professor = profRepo.findByEmail(professorEmail);

     // Fetch the students related to the connected professor
        List<Student> professorStudents = studentRepo.findByGroupeIn(professor.getGroupes());

        // Populate encodedPhotos with encoded images
        List<String> encodedPhotos = new ArrayList<>();
        for (Student student : professorStudents) {
            encodedPhotos.add(encodePhoto(student.getPhoto()));
        }

        model.addAttribute("encodedPhotos", encodedPhotos);

        // Retrieve the connected professor from the session or authentication context

        // Check if a professor is connected
        if (professor == null) {
            // Handle the case where no professor is connected, e.g., redirect to an error page
            return "redirect:/error";
        }

        // Fetch the students related to the connected professor

        model.addAttribute("users", professorStudents);
        model.addAttribute("user", new Student());
        System.out.println("User object: " + model.getAttribute("user"));

        model.addAttribute("mode", "");
        model.addAttribute("groupe", groupeRepo.findAll());

        return "student";
    }

    @GetMapping("/student/adduser")
    public String showAddUserForm(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            // Handle the case where the principal is null, e.g., redirect to the login page
            return "redirect:/login";
        }
     // Récupérer le nom du professeur à partir du Principal
        String professorEmail = principal.getName();

        // Trouver le professeur par son email
        Professor professor = profRepo.findByEmail(professorEmail);


        // Retrieve the connected professor from the session or authentication context

        // Check if a professor is connected
        if (professor == null) {
            // Handle the case where no professor is connected, e.g., redirect to an error page
            return "redirect:/error";
        }

        // Fetch the groups related to the connected professor
        List<Groupe> professorGroupes = professor.getGroupes();

        model.addAttribute("users", studentRepo.findAll());
        model.addAttribute("user", new Student());
        model.addAttribute("mode", "add");
        model.addAttribute("groupes", professorGroupes); // Provide only the relevant groups

        return "student";
    }


    @PostMapping("/student/addUtilisateurr")
    public String adduser(@Validated Student student, BindingResult result, Model model, @RequestParam("photoFile") MultipartFile photoFile){
        if (result.hasErrors()) {
            // If there are validation errors, return to the form with error messages
            return "student";
        }

        // Set the group for the student based on the selected group in the form
        Groupe selectedGroupe = student.getGroupe();
        
        // You might want to add additional validation to ensure the selected group is valid
        
        student.setGroupe(selectedGroupe);
        student.setRole("ROLE_STUDENT");
        String encodedPassword = BCrypt.hashpw(student.getPassword(),BCrypt.gensalt(12));
        student.setPassword(encodedPassword);
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                student.setPhoto(photoFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
       }
}
        User savedUser = studentRepo.save(student);

        return "redirect:/student";
    }
    
    @GetMapping("/student/details/{id}")
    public String showRealisation(@PathVariable("id") long id, Model model, Principal principal){
        List<String> encodedPhotosFront = new ArrayList<>();
        List<String> encodedPhotosSide = new ArrayList<>();	

        List<StudentPW> studentPWList = studentpwRepo.findByStudentId(id);

        for (StudentPW studentpw : studentPWList) {
            encodedPhotosFront.add(encodePhoto(studentpw.getImageFront()));
        }
        for (StudentPW studentpw : studentPWList) {
            encodedPhotosSide.add(encodePhoto(studentpw.getImageSide()));
        }

        model.addAttribute("encodedPhotosFront", encodedPhotosFront);
        model.addAttribute("encodedPhotosSide", encodedPhotosSide);
        model.addAttribute("studentPWList",studentPWList);



       return"studentdetails";}


    @GetMapping("/student/editUser/{id}")
    public String showUpdateUserForm(@PathVariable("id") long id, Model model, Principal principal) {
        User user = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        if (principal == null) {
            // Handle the case where the principal is null, e.g., redirect to the login page
            return "redirect:/login";
        }

        // Récupérer le nom du professeur à partir du Principal
        String professorEmail = principal.getName();

        // Trouver le professeur par son email
        Professor professor = profRepo.findByEmail(professorEmail);

        // Retrieve the connected professor from the session or authentication context

        // Check if a professor is connected
        if (professor == null) {
            // Handle the case where no professor is connected, e.g., redirect to an error page
            return "redirect:/error";
        }

        // Fetch the groups related to the connected professor
        List<Groupe> professorGroupes = professor.getGroupes();

        model.addAttribute("users", studentRepo.findAll());
        model.addAttribute("user", user);
        model.addAttribute("mode", "update");
        model.addAttribute("groupes", professorGroupes); // Provide only the relevant groups

        return "student";
    }


    @PostMapping("/student/updateUser/{id}")
    public String updateuser(@PathVariable("id") Long id, @Validated Student user, BindingResult result, Model model,@RequestParam("photoFile") MultipartFile photoFile) {
        if (result.hasErrors()) {
            user.setId(id);
            return"student";
        }
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                user.setPhoto(photoFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            // If no new photo is provided, keep the existing photo
            Student existingStudent = studentRepo.findById(user.getId()).orElse(null);
            if (existingStudent != null && existingStudent.getPhoto() != null) {
                user.setPhoto(existingStudent.getPhoto());
            }}

		User u = userService.saveUser(user);
        return "redirect:/student";}
        
    

    @GetMapping("/student/deleteUser/{id}")
    public String deleteuser(@PathVariable("id") long id, Model model) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        studentRepo.delete(student);
        return "redirect:/student";
    }}

