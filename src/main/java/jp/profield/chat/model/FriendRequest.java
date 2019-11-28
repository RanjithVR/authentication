package jp.profield.chat.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "friend_request")
public class FriendRequest {
	
	@Id
	private String id;
	
	@Field("requested_by")
	private UserManagement reqByUserId;
	
	@Field("requested_to")
	private String reqToUserId;
	
	@Field("created_on")
	private Date requestOn;
	
	@Field("acted_on")
	private Date actedOn;
	
	
	@Field("actedOrNot")
	private boolean actedOrNot;

	public FriendRequest() {
		super();
	}

	
	public FriendRequest(String id, UserManagement reqByUserId, String reqToUserId,
			Date requestOn, Date actedOn, boolean actedOrNot) {
		super();
		this.id = id;
		this.reqByUserId = reqByUserId;
		this.reqToUserId = reqToUserId;
		this.requestOn = requestOn;
		this.actedOn = actedOn;
		this.actedOrNot = actedOrNot;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserManagement getReqByUserId() {
		return reqByUserId;
	}

	public void setReqByUserId(UserManagement reqByUserId) {
		this.reqByUserId = reqByUserId;
	}

	public String getReqToUserId() {
		return reqToUserId;
	}

	public void setReqToUserId(String reqToUserId) {
		this.reqToUserId = reqToUserId;
	}

	public Date getRequestOn() {
		return requestOn;
	}

	public void setRequestOn(Date requestOn) {
		this.requestOn = requestOn;
	}

	public Date getActedOn() {
		return actedOn;
	}

	public void setActedOn(Date actedOn) {
		this.actedOn = actedOn;
	}


	public boolean isActedOrNot() {
		return actedOrNot;
	}


	public void setActedOrNot(boolean actedOrNot) {
		this.actedOrNot = actedOrNot;
	}


	
	

}
