package com.app.lmsapp.controller;

import java.io.InputStream;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.lmsapp.dto.ResponseDto;
import com.app.lmsapp.model.*;
import com.app.lmsapp.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentInfoRepository stdrepo;

    @Autowired
    private ResponseRepository resrepo;

    @Autowired
    private StudyMaterialRepository smrepo;

    

    @GetMapping("/stdhome")
	public String ShowStudentDashboard(HttpSession session, Model model)
	{
		if(session.getAttribute("studentid")!=null)
		{
			StudentInfo stdinfo = stdrepo.getById((long) session.getAttribute("studentid"));
			model.addAttribute("stdinfo", stdinfo);
			return "student/studentdashboard";
		}
		else {
			return "redirect:/stulogin";
		}
	}
	@PostMapping("/stdhome")
	public String UploadPic(@RequestParam MultipartFile file, RedirectAttributes attributes, HttpSession session)
	{
		try {
			String storageFileName = file.getOriginalFilename();
			String uploadDir = "public/profile/";
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try(InputStream inputStream=file.getInputStream())
			{
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
			}
			StudentInfo std=stdrepo.findById((long)session.getAttribute("studentid")).get();
			std.setProfilepic(storageFileName);
			stdrepo.save(std);
			attributes.addFlashAttribute("msg", "Profile pic uploaded Successfully");
			return "redirect:/student/stdhome";
			
		} catch(Exception e) {
			attributes.addFlashAttribute("msg", "Something went Wrong" +e.getMessage());
			return "redirect:/student/stdhome";
		}
		
	}

	@GetMapping("/studymaterial")
	public String showStudyMaterial(HttpSession session, Model model) {
	    Object studentIdObj = session.getAttribute("studentid");
	    if (studentIdObj == null) {
	        return "redirect:/stulogin";
	    }
	    Long studentId;
	    try {
	        studentId = Long.parseLong(studentIdObj.toString());
	    } catch (NumberFormatException e) {
	        System.out.println("Invalid student ID format in session.");
	        return "redirect:/stulogin";
	    }
	    StudentInfo st = stdrepo.findById(studentId).orElse(null);
	    if (st == null) {
	        System.out.println("Student not found with ID: " + studentId);
	        return "redirect:/stulogin";
	    }
	    model.addAttribute("name", st.getName());
	    String program = st.getProgram();
	    String branch = st.getBranch();
	    String year = st.getYear();
	    String materialtype = "studymaterial";
	    List<StudyMaterial> smlist = smrepo.findAllbyType(program, branch, year, materialtype);
	    if (smlist == null || smlist.isEmpty()) {
	        System.out.println("No study materials found for Program: " + program + ", Branch: " + branch + ", Year: " + year);
	    } else {
	        System.out.println("Found " + smlist.size() + " study materials.");
	    }

	    model.addAttribute("smlist", smlist);
	    return "student/studymaterial";
	}

	@GetMapping("/important")
	public String showAssignment(HttpSession session,Model model) {
		if(session.getAttribute("studentid")!=null) {
			StudentInfo st = stdrepo.getById((long)session.getAttribute("studentid"));
			model.addAttribute("name", st.getName());
			String program = st.getProgram();
			String branch = st.getBranch();
			String year = st.getYear();
			String materialtype="important";
			List<StudyMaterial> smlist = smrepo.findAllbyType(program,branch,year,materialtype);
			model.addAttribute("smlist", smlist);
			return "student/important";
		}else {
			return "redirect:/stulogin";
		}
	}
	
	
	@GetMapping("/giveresponse")
	public String ShowGiveResponse(HttpSession session, Model model)
	{
		if(session.getAttribute("studentid")!=null)
		{
			ResponseDto dto = new ResponseDto();
			model.addAttribute("dto", dto);
			
			return "student/giveresponse";
		}
		else {
			return "redirect:/stulogin";
		}
	}
	
	@PostMapping("/giveresponse")
	public String SubmitResponse(@ModelAttribute ResponseDto dto, HttpSession session, RedirectAttributes attributes)
	{
		if(session.getAttribute("studentid")!=null)
		{
			try {
				
				StudentInfo stdinfo = stdrepo.getById((Long) session.getAttribute("studentid"));
				
				Response res=new Response();
				res.setName(stdinfo.getName());
				//res.setEnrollmentno(stdinfo.getEnrollmentno());
				res.setContactno(stdinfo.getContactno());
				res.setResponsetype(dto.getResponsetype());
				res.setResponsetitle(dto.getResponsetitle());
				res.setResponsetext(dto.getResponsetext());
				Date dt=new Date();
				SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
				String resdate = df.format(dt);
				res.setResdate(resdate);
				resrepo.save(res);
				attributes.addFlashAttribute("msg","Response Submitted Successfully");
				return "redirect:/student/giveresponse";
			
				
			} catch (Exception e) {
				attributes.addFlashAttribute("msg", "Something Went Wrong" +e.getMessage());
				return "redirect:/student/giveresponse";
			}
			
		}
		else {
			return "redirect:/stulogin";
		}
	}
	
	@GetMapping("/changepassword")
	public String showChangePassword(HttpSession session,Model model,RedirectAttributes attributes)
	{
		if(session.getAttribute("studentid")!=null)
		{
			StudentInfo std= stdrepo.getById((long)session.getAttribute("studentid"));
			model.addAttribute("name",std .getName());
			return "student/changepassword";
			}
		else {

			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/stulogin+";
		}
	}
	
	@PostMapping("/changepassword")
	public String ChangePassword(HttpSession session, RedirectAttributes attributes, HttpServletRequest request)
	{
		try {
			StudentInfo stdinfo = stdrepo.findById((long)session.getAttribute("studentid")).get();
			String oldpass=request.getParameter("oldpass");
			String newpass=request.getParameter("newpass");
			String confirmpass=request.getParameter("confirmpass");
			if(newpass.equals(confirmpass))
			{
				if(oldpass.equals(stdinfo.getPassword())) {
					stdinfo.setPassword(confirmpass);
					stdrepo.save(stdinfo);
					session.invalidate();
					attributes.addFlashAttribute("msg", "Password change successfully...");
					return "redirect:/stulogin";
				}else {
					attributes.addFlashAttribute("message", "Invalid old password");
				}
				
			}else {
				attributes.addFlashAttribute("message", "new password and confirm password not match");
			}
			
			return "redirect:/student/changepassword";
		} catch (Exception e) {
			attributes.addFlashAttribute("message", "Something went wrong" +e.getMessage());
			return "redirect:/student/changepassword";
		}
	}
	
	
	@GetMapping("/logout")
	public String Logout(HttpSession session)
	{
		if(session.getAttribute("studentid")!=null)
		{
			
			session.invalidate();
			return "redirect:/student/stdhome";
		}
		else {
			return "redirect:/stulogin";
		}
	}



    
}
