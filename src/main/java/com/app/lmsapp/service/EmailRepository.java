package com.app.lmsapp.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.lmsapp.model.EmailSend;



public interface EmailRepository extends JpaRepository<EmailSend, String>{

}
