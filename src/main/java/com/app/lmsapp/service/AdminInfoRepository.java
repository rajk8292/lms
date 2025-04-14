package com.app.lmsapp.service;



import org.springframework.data.jpa.repository.JpaRepository;

import com.app.lmsapp.model.AdminInfo;

public interface AdminInfoRepository extends JpaRepository<AdminInfo, String> {
	

}
