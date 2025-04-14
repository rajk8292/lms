package com.app.lmsapp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.lmsapp.API.EmailService;
import com.app.lmsapp.dto.AdminInfoDto;
import com.app.lmsapp.dto.EmailSendDto;
import com.app.lmsapp.dto.StudentInfoDto;
import com.app.lmsapp.model.AdminInfo;
import com.app.lmsapp.model.EmailSend;
import com.app.lmsapp.model.StudentInfo;
import com.app.lmsapp.service.AdminInfoRepository;
import com.app.lmsapp.service.EmailRepository;
import com.app.lmsapp.service.StudentInfoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	StudentInfoRepository stdrepo;

	@Autowired
	AdminInfoRepository adrepo;

	@Autowired
	EmailRepository esrepo;

	@Autowired
	private EmailService emailService;

	@GetMapping("/")
	public String showIndex() {
		return "index";
	}

	@Controller
	public class ContactController {

		@GetMapping("/contactus")
		public String showContactUs(Model model) {
			EmailSendDto dto = new EmailSendDto();
			model.addAttribute("dto", dto);
			return "contactus";
		}

		@PostMapping("/contactus")
		public String contactUs(@ModelAttribute EmailSendDto dto, RedirectAttributes attrib) {
			try {
				EmailSend eninfo = new EmailSend();
				eninfo.setName(dto.getName());
				eninfo.setEmail(dto.getEmail());
				eninfo.setMessage(dto.getMessage());
				esrepo.save(eninfo);
				emailService.SendEmail(dto.getName(), dto.getEmail(), dto.getMessage());
				attrib.addFlashAttribute("msg", "Send your message Successfully");
				return "redirect:/contactus";
			} catch (Exception e) {
				attrib.addFlashAttribute("msg", "something went Wrong" + e.getMessage());
				return "redirect:/contactus";
			}
		}
	}

	@GetMapping("/registration")
	public String showRegistration(Model model) {
		StudentInfoDto dto = new StudentInfoDto();
		model.addAttribute("dto", dto);
		return "registration";
	}

	@PostMapping("/registration")
	public String Registration(@ModelAttribute StudentInfoDto dto, RedirectAttributes attrib) {
		try {
			StudentInfo stdinfo = new StudentInfo();
			stdinfo.setEnrollmentno(dto.getEnrollmentno());
			stdinfo.setName(dto.getName());
			stdinfo.setProgram(dto.getProgram());
			stdinfo.setBranch(dto.getBranch());
			stdinfo.setYear(dto.getYear());
			stdinfo.setContactno(dto.getContactno());
			stdinfo.setEmailaddress(dto.getEmailaddress());
			stdinfo.setPassword(dto.getPassword());
			Date dt = new Date();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String regdate = df.format(dt);
			stdinfo.setRegdate(regdate);
			stdrepo.save(stdinfo);
			attrib.addFlashAttribute("msg", "Registration Successfull! and Send mail on your E-mail");
			return "redirect:/registration";
		} catch (Exception e) {
			attrib.addFlashAttribute("msg", "something went Wrong" + e.getMessage());
			return "redirect:/registration";
		}
	}

	@GetMapping("/stulogin")
	public String showStudentLogin(Model model) {

		StudentInfoDto dto = new StudentInfoDto();
		model.addAttribute("dto", dto);
		return "stulogin";
	}

	@PostMapping("/stulogin")
	public String StudentLogin(@ModelAttribute StudentInfoDto dto, RedirectAttributes attributes, HttpSession session) {
		try {
			StudentInfo stdinfo = stdrepo.getById(dto.getEnrollmentno());
			if (stdinfo.getPassword().equals(dto.getPassword())) {
				session.setAttribute("studentid", dto.getEnrollmentno());
				return "redirect:/student/stdhome";
			} else {
				attributes.addFlashAttribute("msg", "Invalid Users");
				return "redirect:/stulogin";
			}

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "User Does not Exist");
			return "redirect:/stulogin";
		}

	}

	@GetMapping("/adminlogin")
	public String showAdminLogin(Model model) {

		AdminInfoDto dto = new AdminInfoDto();
		model.addAttribute("dto", dto);
		return "adminlogin";
	}

	@PostMapping("/adminlogin")
	public String AdminLogin(@ModelAttribute AdminInfoDto dto, HttpSession session, RedirectAttributes attributes) {

		try {

			AdminInfo ad = adrepo.getById(dto.getUserid());
			if (ad.getPassword().equals(dto.getPassword())) {
				// attributes.addFlashAttribute("msg","Valid User");
				session.setAttribute("admin", ad.getUserid().toString());
				return "redirect:/admin/adhome";
			} else {
				attributes.addFlashAttribute("msg", "Invalid User");
				return "redirect:/adminlogin";
			}

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "User does not exists " + e.getMessage());
			return "redirect:/adminlogin";
		}

	}
}