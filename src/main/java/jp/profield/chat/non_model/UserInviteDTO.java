package jp.profield.chat.non_model;

import jp.profield.chat.model.UserManagement;

public class UserInviteDTO {

	
	private String status;
	private UserManagement user;
	private boolean alreadyFrnd;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UserManagement getUser() {
		return user;
	}
	public void setUser(UserManagement user) {
		this.user = user;
	}
	public boolean isAlreadyFrnd() {
		return alreadyFrnd;
	}
	public void setAlreadyFrnd(boolean alreadyFrnd) {
		this.alreadyFrnd = alreadyFrnd;
	}
	
	
	
}
