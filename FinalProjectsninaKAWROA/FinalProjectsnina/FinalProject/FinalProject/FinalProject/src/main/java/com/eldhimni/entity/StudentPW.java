package com.eldhimni.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPW {
    @EmbeddedId
    private StudentPWPK id;
    private String time;
    @Temporal(TemporalType.DATE)
    private Date date;
    @ManyToOne
    @JoinColumn(name="student_id",referencedColumnName="id",insertable = false,updatable = false)
    private Student student;
    @ManyToOne
    @JoinColumn(name="pw_id",referencedColumnName="id",insertable = false,updatable = false)
    private PW pw;
    @Lob
    @Column(name = "imageFront", columnDefinition = "LONGBLOB")
    private byte[] imageFront;

    @Transient
    @JsonIgnore
    private MultipartFile imageFrontFile;
    
    @Lob
    @Column(name = "imageSide", columnDefinition = "LONGBLOB")
    private byte[] imageSide;

    @Transient
    @JsonIgnore
    private MultipartFile imageSideFile;

    public byte[] getImageFront() {
        return imageFront;
    }

    public void setImageFront(byte[] imageFront) {
        this.imageFront = imageFront;
    }

	public StudentPWPK getId() {
		return id;
	}

	public void setId(StudentPWPK id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public PW getPw() {
		return pw;
	}

	public void setPw(PW pw) {
		this.pw = pw;
	}

	public MultipartFile getImageFrontFile() {
		return imageFrontFile;
	}

	public void setImageFrontFile(MultipartFile imageFrontFile) {
		this.imageFrontFile = imageFrontFile;
	}

	public byte[] getImageSide() {
		return imageSide;
	}

	public void setImageSide(byte[] imageSide) {
		this.imageSide = imageSide;
	}

	public MultipartFile getImageSideFile() {
		return imageSideFile;
	}

	public void setImageSideFile(MultipartFile imageSideFile) {
		this.imageSideFile = imageSideFile;
	}

}
