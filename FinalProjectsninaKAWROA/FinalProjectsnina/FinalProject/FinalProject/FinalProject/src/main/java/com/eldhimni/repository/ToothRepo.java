package com.eldhimni.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eldhimni.entity.Professor;
import com.eldhimni.entity.Tooth;

@Repository
public interface ToothRepo extends CrudRepository<Tooth,Long>{}




