package com.app.lmsapp.dto;

import org.springframework.web.multipart.MultipartFile;

public class StudentInfoDto {
	private long enrollmentno;
	private String name;
	private String program;
	private String branch;
	private String year;
	private String contactno;
	private String emailaddress;
	private String password;
	private String regdate;
	private MultipartFile  profilepic;
	public MultipartFile getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(MultipartFile profilepic) {
		this.profilepic = profilepic;
	}
	public long getEnrollmentno() {
		return enrollmentno;
	}
	public void setEnrollmentno(long enrollmentno) {
		this.enrollmentno = enrollmentno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getContactno() {
		return contactno;
	}
	public void setContactno(String contactno) {
		this.contactno = contactno;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
    public String getEmail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmail'");
    }
}
