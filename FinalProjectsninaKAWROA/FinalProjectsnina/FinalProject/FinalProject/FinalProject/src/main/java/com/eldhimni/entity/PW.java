package com.eldhimni.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PW {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String objectif;
    @Lob
    @Column(name = "docs", columnDefinition = "BLOB")
    private byte[] docs;

    @Transient
    @JsonIgnore
    private MultipartFile docsFile;    
    @ManyToOne
    private Tooth tooth;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "pw_group",
        joinColumns = @JoinColumn(name = "pw_id"),
        inverseJoinColumns = @JoinColumn(name = "groupe_id")
    )
private Set<Groupe> groupes=new HashSet<>();    


	public byte[] getDocs() {
		return docs;
	}
	public MultipartFile getDocsFile() {
		return docsFile;
	}
	public void setDocsFile(MultipartFile docsFile) {
		this.docsFile = docsFile;
	}
	public void setDocs(byte[] docs) {
		this.docs = docs;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getObjectif() {
		return objectif;
	}
	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}
	
	public Tooth getTooth() {
		return tooth;
	}
	public void setTooth(Tooth tooth) {
		this.tooth = tooth;
	}
	public Set<Groupe> getGroupes() {
		return groupes;
	}
	public void setGroupes(Set<Groupe> groupes) {
		this.groupes = groupes;
	}}
