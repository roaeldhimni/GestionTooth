package com.eldhimni.entity;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Professor extends User {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
	    @JsonIgnore
        private List<Groupe> groupes;

	    public List<Groupe> getGroupes() {
			return groupes;
		}

		public void setGroupes(List<Groupe> groupes) {
			this.groupes = groupes;
		}

		// Ajoutez le getter approprié
	    public Long getId() {
	        return id;
	    }

	    // Ajoutez le setter si nécessaire
	    public void setId(Long id) {
	        this.id = id;
	    }

	
    private String grade;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

	
}
