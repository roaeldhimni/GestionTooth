package com.eldhimni.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eldhimni.entity.Professor;

@Repository
public interface ProfessorRepo extends CrudRepository<Professor,Long>{
    Professor findByEmail(String email);




}