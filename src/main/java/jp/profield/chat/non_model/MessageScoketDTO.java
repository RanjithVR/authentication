package jp.profield.chat.non_model;

import jp.profield.chat.model.Group;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.web.rest.dto.IndividualChatDTO;
import jp.profield.chat.web.rest.dto.GroupMessageDTO;
public class MessageScoketDTO {
	
	private IndividualChatDTO individualChatDTO;
	private String status;
	private GroupMessageDTO groupMessageDTO;
	private long senderUk;
	private boolean onlineStatus;
	private String senderUserId;
	private Group group;
	
	private UserManagement userManagement;
	
	
	public MessageScoketDTO() {
		super();
	}
	
	public IndividualChatDTO getIndividualChatDTO() {
		return individualChatDTO;
	}
	public void setIndividualChatDTO(IndividualChatDTO individualChatDTO) {
		this.individualChatDTO = individualChatDTO;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public GroupMessageDTO getGroupMessageDTO() {
		return groupMessageDTO;
	}

	public void setGroupMessageDTO(GroupMessageDTO groupMessageDTO) {
		this.groupMessageDTO = groupMessageDTO;
	}

	public long getSenderUk() {
		return senderUk;
	}

	public void setSenderUk(long senderUk) {
		this.senderUk = senderUk;
	}

	public boolean isOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public UserManagement getUserManagement() {
		return userManagement;
	}

	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	
}
