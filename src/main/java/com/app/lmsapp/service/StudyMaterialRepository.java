package com.app.lmsapp.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.app.lmsapp.model.StudyMaterial;



@Repository
public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Integer> {

	@Query("select m from StudyMaterial m where m.program=:program and m.branch=:branch and m.year=:year and m.materialtype=:materialtype")       
	List<StudyMaterial> findAllbyType(String program, String branch, String year, String materialtype);

	@Query("select m from StudyMaterial m where m.materialtype=:materialtype")
	List<StudyMaterial> findByMaterialType(@Param("materialtype") String materialtype);

	

	
}


