package jp.profield.chat.service;

public interface MailSendService {
	public void sendingInvitationToNewUser(String emailId,String subject,
			String messageContentOTP, String currentUserFullName);
	public void sendWelcomeMailToNewUser(String emailId, String userName,String password,String userFullName);
	
	public void sendTokenForForgotPassword(String userFullName,String emailId, long token);
}
