package com.eldhimni.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eldhimni.entity.StudentPW;

@Repository
public interface StudentPWRepo extends CrudRepository<StudentPW,Long>{

	List<StudentPW> findByStudentId(Long id);

}