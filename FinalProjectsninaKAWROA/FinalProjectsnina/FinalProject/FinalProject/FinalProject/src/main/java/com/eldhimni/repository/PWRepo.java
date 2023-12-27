package com.eldhimni.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eldhimni.entity.Groupe;
import com.eldhimni.entity.PW;

@Repository
public interface PWRepo extends CrudRepository<PW,Long>{

	Set<PW> findByGroupes(Groupe groupe);

}