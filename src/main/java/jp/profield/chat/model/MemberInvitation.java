package jp.profield.chat.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document(collection = "member_invite")
public class MemberInvitation {

	@Id
	private String id;
	
	
	@DBRef
	private UserManagement user;
	
	@Field("invitee_email")
	private String email;
	
	@Field("invited_on")
	private Date invitedOn;

	public MemberInvitation() {
		super();
	
	}

	public MemberInvitation(String id, UserManagement user, String email,
			Date invitedOn) {
		super();
		this.id = id;
		this.user = user;
		this.email = email;
		this.invitedOn = invitedOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserManagement getUser() {
		return user;
	}

	public void setUser(UserManagement user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getInvitedOn() {
		return invitedOn;
	}

	public void setInvitedOn(Date invitedOn) {
		this.invitedOn = invitedOn;
	}
	
	
	
	
	
}
 