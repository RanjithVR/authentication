package jp.profield.chat.api.dto;


import java.time.LocalDate;
import java.util.Date;

import jp.profield.chat.model.FriendRequest;

public class FriendRequestDTO {

	private String userId;
	private String userFullName;
	private String attachmentId;
	private Date requestedOn;
	private String friendRequestId;
	private String profileUrl;
	
	public FriendRequestDTO(FriendRequest friendRequest) {
		super();
		this.userId = friendRequest.getReqByUserId().getId();
		this.userFullName = friendRequest.getReqByUserId().getFirstName() +" "+ friendRequest.getReqByUserId().getLastName();
		this.attachmentId = friendRequest.getReqByUserId().getAttachment().getId();
		this.requestedOn = friendRequest.getRequestOn();
		this.friendRequestId = friendRequest.getId();
		this.profileUrl=friendRequest.getReqByUserId().getAttachment().getLocation();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Date getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}

	public String getFriendRequestId() {
		return friendRequestId;
	}

	public void setFriendRequestId(String friendRequestId) {
		this.friendRequestId = friendRequestId;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
	
	
}
