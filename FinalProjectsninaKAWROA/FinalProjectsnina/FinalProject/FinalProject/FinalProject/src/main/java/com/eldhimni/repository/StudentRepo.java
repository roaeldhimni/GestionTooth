package com.eldhimni.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.Professor;
import com.eldhimni.entity.Student;

@Repository
public interface StudentRepo extends CrudRepository<Student,Long>{

	List<Student> findByGroupeIn(List<Groupe> groupes);

	Set<Student> findByGroupe(Groupe groupe);


}