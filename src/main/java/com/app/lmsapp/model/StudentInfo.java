package com.app.lmsapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="studentinfo")
public class StudentInfo {
	
	@Id
	private long enrollmentno;
	@Column(length=50, nullable=false)
	private String name;
	@Column(length=100, nullable=false)
	private String program;
	@Column(length=50, nullable=false)
	private String branch;
	@Column(length=50, nullable=false)
	private String year;
	@Column(length=50, nullable=false)
	private String emailaddress;
	@Column(length=50, nullable=false)
	private String contactno;
	@Column(length=50, nullable=false)
	private String password;
	@Column(length=50, nullable=false)
	private String regdate;
	@Column(nullable = true, length = 1000)
	private String profilepic;
	
	public String getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(String profilepic) {
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
	
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
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

}
