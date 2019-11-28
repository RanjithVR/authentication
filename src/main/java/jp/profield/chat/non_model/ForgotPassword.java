package jp.profield.chat.non_model;


public class ForgotPassword {
	
	private	 long tocken;
	private  String password;
	private  String emailId;
	
	
	public ForgotPassword() {
		super();
		
	}
	public long getTocken() {
		return tocken;
	}
	public void setTocken(long tocken) {
		this.tocken = tocken;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	
}
