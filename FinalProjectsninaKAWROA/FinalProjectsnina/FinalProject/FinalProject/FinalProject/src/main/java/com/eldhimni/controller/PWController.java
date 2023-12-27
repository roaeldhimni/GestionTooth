package com.eldhimni.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.PW;
import com.eldhimni.entity.Tooth;
import com.eldhimni.repository.GroupeRepo;
import com.eldhimni.repository.PWRepo;
import com.eldhimni.repository.ToothRepo;

@Controller
public class PWController {

	@Autowired
	private PWRepo pwRepository;


    @Autowired
    private GroupeRepo groupeRepository;

    @Autowired
    private ToothRepo toothRepository;
    
   
  
//    @GetMapping("/pw")
//    public String showPWList(Model model) {
//        // Récupérez la liste des PWs depuis la base de données
//        List<PW> pwList = (List<PW>) pwRepository.findAll();
//
//        // Ajoutez la liste des PWs au modèle
//        model.addAttribute("pwList", pwList);
//
//        return "pwList";  // Remplacez "pwList" par le nom de votre fichier HTML pour afficher la liste des PWs
//    }
    
    @GetMapping("/pw/home")
    public String showHome(Model model) {
        Iterable<Groupe> allGroupes = groupeRepository.findAll();
        List<Groupe> groupesList = new ArrayList<>();
        allGroupes.forEach(groupesList::add);

        List<PW> pwList = (List<PW>) pwRepository.findAll();

        model.addAttribute("groupes", groupesList);
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("pws", pwList);
        model.addAttribute("pw", new PW());
        model.addAttribute("professors", pwRepository.findAll());

        return "pwhome";
    }
    

    @GetMapping("/pw/add")
    public String showAddPWForm(Model model) {
        model.addAttribute("groupes", groupeRepository.findAll());
        model.addAttribute("teeth", toothRepository.findAll()); // Assurez-vous d'avoir un repository pour Tooth
        model.addAttribute("pw", new PW());
        model.addAttribute("mode", "add");
        return "pwhome"; // Remplacez par le nom de votre fichier HTML pour la gestion des PWs
    }
    @PostMapping("/pw/addadd")
    public String addPW(@Validated PW pw, BindingResult result, Model model,
            @RequestParam("selectedGroupes") List<Long> selectedGroupesIds,
            @RequestParam("docsFile") MultipartFile docsFile) throws IOException {
//        if (result.hasErrors()) {
//            return "pwhome";
//        }

        // Enregistrez le PW dans la base de données
        PW savedPW = pwRepository.save(pw);

        // Associez les groupes sélectionnés avec le PW
        List<Groupe> selectedGroupes = (List<Groupe>) groupeRepository.findAllById(selectedGroupesIds);

        if (selectedGroupes != null && !selectedGroupes.isEmpty()) {
            savedPW.getGroupes().addAll(selectedGroupes);
        }
        
     // Handle file upload
        if (docsFile != null && !docsFile.isEmpty()) {
            byte[] docsBytes = docsFile.getBytes();
            pw.setDocs(docsBytes);
            
            
           
        }
        
       

       

        // Mettez à jour les champs objectif et docs
        savedPW.setObjectif(pw.getObjectif());
        savedPW.setDocs(pw.getDocs());

        // Enregistrez à nouveau le PW pour mettre à jour la relation many-to-many et les nouveaux champs
        pwRepository.save(savedPW);

        System.out.println("Selected Group IDs: " + selectedGroupesIds);

        return "redirect:/pw/home";}
    
    @PostMapping("/pw/delete/{id}")
    public String deletePW(@PathVariable Long id) {
        // Supprimez le PW de la base de données
        pwRepository.deleteById(id);

        return "redirect:/pw/home";
    }
    
    @GetMapping("/pw/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        Optional<PW> optionalPW = pwRepository.findById(id);

        if (optionalPW.isPresent()) {
            PW pw = optionalPW.get();
            byte[] fileContent = pw.getDocs();
            //crée une ressource ByteArrayResource à partir du contenu du fichier, qui est représenté par le tableau d'octets fileContent
            ByteArrayResource resource = new ByteArrayResource(fileContent);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + pw.getTitle() + ".doc")
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileContent.length)
                    .body(resource);
        } else {
            // Handle the case where PW is not found
            return ResponseEntity.notFound().build();}}
    
    @GetMapping("/pw/edit/{id}")
    public String showEditPWForm(@PathVariable Long id, Model model) {
        // Chargez le PW depuis la base de données
        Optional<PW> optionalPW = pwRepository.findById(id);

        if (optionalPW.isPresent()) {
            PW pw = optionalPW.get();
            model.addAttribute("pw", pw);
            model.addAttribute("groupes", groupeRepository.findAll());
            model.addAttribute("teeth", toothRepository.findAll());
            model.addAttribute("mode", "update"); // Utilisez "update" pour indiquer que c'est une opération de mise à jour
            return "pwhome";
        } else {
            // Gérer le cas où le PW n'est pas trouvé
            return "redirect:/pw/home";
        }
    }

    
   
    @PostMapping("/pw/update/{id}")
    public String updatePW(@PathVariable("id") long id, @Validated PW pw, BindingResult result, Model model,
                           @RequestParam List<Long> selectedGroupes,
                           @RequestParam (name = "docsFile", required = false) MultipartFile docsFile) {
        if (result.hasErrors()) {
            pw.setId(id);
            return "pwhome";
        }

        // Load the PW from the database
        Optional<PW> optionalPW = pwRepository.findById(id);

        if (optionalPW.isPresent()) {
            PW existingPW = optionalPW.get();
            existingPW.setTitle(pw.getTitle());
            existingPW.setObjectif(pw.getObjectif());

            // Update the document only if a new file is provided
            if (docsFile != null && !docsFile.isEmpty()) {
                try {
                    existingPW.setDocs(docsFile.getBytes());
                } catch (IOException e) {
                    // Handle the exception as needed
                    e.printStackTrace();
                }
            }

            // Load the groups from the database
            List<Groupe> selectedGroupesList = (List<Groupe>) groupeRepository.findAllById(selectedGroupes);

            // Update the groups associated with the PW
            existingPW.setGroupes(new HashSet<>(selectedGroupesList));

            // Save the modified PW in the database
            pwRepository.save(existingPW);
        }

        return "redirect:/pw/home";
}}