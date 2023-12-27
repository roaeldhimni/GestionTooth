package com.eldhimni.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.Professor;

@Repository
public interface GroupeRepo extends CrudRepository<Groupe,Long>{

	List<Groupe> findByProfessor(Professor loggedInProfessor);


}