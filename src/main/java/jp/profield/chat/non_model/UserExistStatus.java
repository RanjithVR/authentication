package jp.profield.chat.non_model;

import jp.profield.chat.model.GroupMessage;
import jp.profield.chat.model.UserManagement;

public class UserExistStatus {
	
	private String status;
	private String groupId;
	private GroupMessage groupMessage;
	private UserManagement userManagement;
	private String userId;
	private long userUKId;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public UserExistStatus() {
		
	}
	public GroupMessage getGroupMessage() {
		return groupMessage;
	}
	public void setGroupMessage(GroupMessage groupMessage) {
		this.groupMessage = groupMessage;
	}
	public UserManagement getUserManagement() {
		return userManagement;
	}
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getUserUKId() {
		return userUKId;
	}
	public void setUserUKId(long userUKId) {
		this.userUKId = userUKId;
	}
	

}
