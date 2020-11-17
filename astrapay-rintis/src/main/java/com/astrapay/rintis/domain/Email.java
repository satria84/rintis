package com.astrapay.rintis.domain;

public class Email implements java.io.Serializable {

	private static final long serialVersionUID = 3721132903100894808L;

	private String emailFrom;
	private String emailTo;
	private String emailSubject;
	private String emailBody;
	private String [] emailCC;
	private String [] emailBCC;
	
	public void setEmailFrom(String emailFrom){
		this.emailFrom = emailFrom;
	}
	
	public String getEmailFrom(){
		return emailFrom;
	}
	
	public void setEmailTo(String emailTo){
		this.emailTo = emailTo;
	}
	
	public String getEmailTo(){
		return emailTo;
	}
	
	public void setEmailSubject(String emailSubject){
		this.emailSubject = emailSubject;
	}
	
	public String getEmailSubject(){
		return emailSubject;
	}
	
	public void setEmailBody(String emailBody){
		this.emailBody = emailBody;
	}
	
	public String getEmailBody(){
		return emailBody;
	}
	
	public void setEmailCC(String [] emailCC){
		this.emailCC = emailCC;
	}
	
	public String [] getEmailCC(){
		return emailCC;
	}
	
	public void setEmailBCC(String [] emailBCC){
		this.emailBCC = emailBCC;
	}
	
	public String [] getEmailBCC(){
		return emailBCC;
	}
}