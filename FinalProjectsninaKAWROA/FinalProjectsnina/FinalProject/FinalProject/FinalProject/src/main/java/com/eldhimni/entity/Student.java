package com.eldhimni.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data

@Entity
public class Student extends User{
	@JsonIgnore
	@ManyToOne
    private Groupe groupe;
	
	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;


    @Transient
    @JsonIgnore
    private MultipartFile photoFile;
    private String number;

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public MultipartFile getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(MultipartFile photoFile) {
		this.photoFile = photoFile;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
    

}
