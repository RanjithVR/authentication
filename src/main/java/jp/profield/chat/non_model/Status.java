package jp.profield.chat.non_model;

import jp.profield.chat.model.UserManagement;

public class Status {

	private String message;
	private String userId; 
	
	
	private UserManagement userObj;

	public Status() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserManagement getUserObj() {
		return userObj;
	}

	public void setUserObj(UserManagement userObj) {
		this.userObj = userObj;
	}
	
	
	
	
}
