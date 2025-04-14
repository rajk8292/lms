package com.app.lmsapp.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.lmsapp.dto.StudyMaterialDto;
import com.app.lmsapp.model.AdminInfo;
import com.app.lmsapp.model.Response;
import com.app.lmsapp.model.StudentInfo;
import com.app.lmsapp.model.StudyMaterial;
import com.app.lmsapp.service.AdminInfoRepository;
import com.app.lmsapp.service.ResponseRepository;
import com.app.lmsapp.service.StudentInfoRepository;
import com.app.lmsapp.service.StudyMaterialRepository;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminInfoRepository adrepo;

    @Autowired
    StudentInfoRepository stdrepo;

    @Autowired
    ResponseRepository resrepo;

    @Autowired
    StudyMaterialRepository smrepo;

    @GetMapping("/adhome")
	public String showAdminDashboard(HttpSession session, RedirectAttributes attribute, Model model) {
		if(session.getAttribute("admin")!=null) {
			
			AdminInfo ad= adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("name",ad.getName());
			
			int scount = (int)stdrepo.count();
			model.addAttribute("scount", scount);
			
			List<StudyMaterial> smlist = smrepo.findByMaterialType("studymaterial");
			int smcount = smlist.size();
			model.addAttribute("smcount", smcount);
			
			List<StudyMaterial> aslist = smrepo.findByMaterialType("important");
			int ascount = aslist.size();
			model.addAttribute("ascount", ascount);
			
			List<Response> flist = resrepo.findByResponseType("feedback");
			int fcount = flist.size();
			model.addAttribute("fcount", fcount);
			
			List<Response> clist = resrepo.findByResponseType("complaint");
			int ccount = clist.size();
			model.addAttribute("ccount", ccount);
			
			
			
			return "admin/admindashboard";
		}
		else {
			attribute.addFlashAttribute("msg","Session Expired");
			return "redirect:/adminlogin";
		}
	}
	
    @GetMapping("/logout")
    public String Logout(HttpSession session, RedirectAttributes attributes) {
        session.invalidate();
        attributes.addFlashAttribute("msg", "Successfully Logout");
        return "redirect:/adminlogin";
    }

    @GetMapping("/StudentManagement")
	public String showStudentManagement(HttpSession session, RedirectAttributes attributes, Model model) {
		if(session.getAttribute("admin")!=null)
		{
			AdminInfo ad= adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("name",ad.getName());
			
			List<StudentInfo> slist = stdrepo.findAll();
			model.addAttribute("slist", slist);
			
			
			return "admin/StudentManagement";
		}
		else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

    @GetMapping("/AddStudyMaterial")
	public String showAddStudyMaterial(HttpSession session, RedirectAttributes attributes, Model model) {
		if(session.getAttribute("admin")!=null)
		{
			AdminInfo ad=adrepo.getById(session.getAttribute("admin").toString());
		   
		    model.addAttribute("name", ad.getName());
		    StudyMaterialDto dto=new StudyMaterialDto();
		    model.addAttribute("dto", dto);
			return "admin/AddStudyMaterial";
		}
		else
		{
			attributes.addFlashAttribute("msg", "Session Expired");
			return "redirect:/adminlogin";
		}
	}
	
	@PostMapping("/AddStudyMaterial")
	public String AddStudyMaterial(@ModelAttribute StudyMaterialDto dto, RedirectAttributes attributes)
	{
		try {
			
			MultipartFile filedata = dto.getFilename();
			String storageFileName = filedata.getOriginalFilename();
			long size = filedata.getSize();
			int s =(int) size/(2048*2048);	//file size in MB
			if(s>10)
			{
				attributes.addFlashAttribute("msg", "File Should be less that 10 MB");
				return "redirect:/admin/AddStudyMaterial";
			}
			System.err.println(size);
			String uploadDir = "public/mat/";
			Path UploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(UploadPath))
			{
				Files.createDirectories(UploadPath);
			}
			
			try(InputStream inputStream = filedata.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir +storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			
			StudyMaterial material = new StudyMaterial();
			material.setProgram(dto.getProgram());;
			material.setBranch(dto.getBranch());
			material.setYear(dto.getYear());
			material.setSubject(dto.getSubject());
			material.setTopic(dto.getTopic());
			material.setMaterialtype(dto.getMaterialtype());
			material.setFilename(storageFileName);
			
			Date dt = new Date();
			SimpleDateFormat df = new SimpleDateFormat();
			String posteddate = df.format(dt);
			material.setPosteddate(posteddate);
			smrepo.save(material);
			attributes.addFlashAttribute("msg", "Material Uploaded Successfully");
			return "redirect:/admin/AddStudyMaterial";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong "+e.getMessage());
			return "redirect:/admin/AddStudyMaterial";
		}
	}
	

	
	@GetMapping("/ManageStudyMaterial")
	public String showManageStudyMaterial(HttpSession session, RedirectAttributes attributes, Model model) {
		if(session.getAttribute("admin")!=null)
		{
			AdminInfo ad = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("name", ad.getName());
			
			List<StudyMaterial> stdm = smrepo.findAll();
			model.addAttribute("stdm", stdm);
			
			return "admin/ManageStudyMaterial";
		}
		else {
			attributes.addFlashAttribute("msg", "Session Expired");
			return "redirect:/adminlogin";
		}
		
	}
	
	@GetMapping("/deletestudymaterial")
	public String DeleteManageStudyMaterial(@RequestParam int id, HttpSession session, RedirectAttributes attributes,Model model)
	{
		
		if(session.getAttribute("admin")!=null)
		{
			StudyMaterial sm = smrepo.findById(id).get();
			smrepo.delete(sm);
			attributes.addFlashAttribute("msg", sm.getFilename() + "is deleted succesfully");
			return "redirect:/admin/ManageStudyMaterial";                                              
		}                                                                                                        
		else
		{
			attributes.addFlashAttribute("msg", "Session Expired");
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/ViewFeedback")
	public String showViewFeedback(HttpSession session, RedirectAttributes attributes, Model model) {
		if(session.getAttribute("admin")!=null)
		{
			AdminInfo ad = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("name", ad.getName());
			 List<Response> flist =resrepo.findByResponseType("feedback");
			 model.addAttribute("flist", flist);
			 return "admin/ViewFeedback";
			 
		 }
		 else {
			 attributes.addFlashAttribute("msg", "Session Expired");
			 return "redirect:/adminlogin";
		 
		}
	}
	@GetMapping("/ViewComplaint")
	public String showViewComplaint(HttpSession session, RedirectAttributes attributes, Model model) {
		 if(session.getAttribute("admin")!=null)
		 {
			 AdminInfo ad = adrepo.getById(session.getAttribute("admin").toString());
			 
			 model.addAttribute("name", ad.getName());
			 List<Response> clist =resrepo.findByResponseType("complaint");
			 model.addAttribute("clist", clist);
			 return "admin/ViewComplaint";
			 
		 }
		 else {
			 attributes.addFlashAttribute("msg", "Session Expired");
			 return "redirect:/adminlogin";
		 }
			
	}
	
	
	@GetMapping("/changepassword")
	public String showChangePassword(HttpSession session,Model model,RedirectAttributes attributes)
	{
		if(session.getAttribute("admin")!=null)
		{
			AdminInfo ad= adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("name", ad.getName());
			return "admin/changepassword";
			}
		else {

			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}
	
	@PostMapping("/changepassword")
	public String ChangePassword(HttpSession session, RedirectAttributes attributes, HttpServletRequest request)
	{
		try {
			AdminInfo adinfo=adrepo.findById(session.getAttribute("admin").toString()).get();
			String oldpass=request.getParameter("oldpass");
			String newpass=request.getParameter("newpass");
			String confirmpass=request.getParameter("confirmpass");
			if(newpass.equals(confirmpass))
			{
				if(oldpass.equals(adinfo.getPassword())) {
					adinfo.setPassword(confirmpass);
					adrepo.save(adinfo);
					session.invalidate();
					attributes.addFlashAttribute("msg", "Password change successfully...");
					return "redirect:/adminlogin";
				}else {
					attributes.addFlashAttribute("message", "Invalid old password");
				}
				
			}else {
				attributes.addFlashAttribute("message", "new password and confirm password not match");
			}
			
			return "redirect:/admin/changepassword";
		} catch (Exception e) {
			attributes.addFlashAttribute("message", "Something went wrong" +e.getMessage());
			return "redirect:/admin/changepassword";
		}
	}
	 
	
	
	@GetMapping("/deletefeedback")
	public String DeleteFeedback(@RequestParam int id, HttpSession session, RedirectAttributes attributes,Model model)
	{	
		
			System.err.println("Alright !!! before");
		     Response res = resrepo.findById(id).get();
			resrepo.delete(res);
			System.err.println("Alright !!! after");
			//attributes.addFlashAttribute("msg", en.getName() + "is deleted succesfully");
			return "redirect:/admin/ViewFeedback";                                              
	}
	
	@GetMapping("/delete")
	public String DeleteStudent(@RequestParam long enroll, HttpSession session, RedirectAttributes attributes,Model model)
	{
		
		if(session.getAttribute("admin")!=null)
		{
			StudentInfo stdinfo=stdrepo.findById(enroll).get();
			stdrepo.delete(stdinfo);
			attributes.addFlashAttribute("msg", stdinfo.getName() + "is deleted succesfully");
			return "redirect:/admin/StudentManagement";
		}
		else
		{
			attributes.addFlashAttribute("msg", "Session Expired");
			return "redirect:/adminlogin";
		}
	}
}


    
