package com.eldhimni.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Groupe {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;
	    private String code;
	    private String year;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "professor_id")
	    private Professor professor;

	    @ManyToMany(mappedBy = "groupes")
	    private Set<PW> pws = new HashSet<>();


	   


	public long getId() {
		return id;
	}

	
	public void setId(long id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}


	public Set<PW> getPws() {
		return pws;
	}


	public void setPws(Set<PW> pws) {
		this.pws =pws;
	}

	
	

}
