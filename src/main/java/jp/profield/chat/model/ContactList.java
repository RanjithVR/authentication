package jp.profield.chat.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "contact_list")
public class ContactList {

	@Id
	private String id;
	
	@Field("user_id")
	private UserManagement userId;
	
	
	@Field("friend_id")
	private UserManagement friendId;
	
	@Field("is_deleted")
	private boolean isDeleted = false;
	
	@Field("deleted_by")
	private String deletedBy;
	
	
	@Field("deleted_on")
	private Date deletedOn;
	
	@Field("created_on")
	private Date createdOn;

	public ContactList() {
		super();
	}

	public ContactList(String id, UserManagement userId, UserManagement friendId,
			boolean isDeleted, String deletedBy, Date deletedOn, Date createdOn) {
		super();
		this.id = id;
		this.userId = userId;
		this.friendId = friendId;
		this.isDeleted = isDeleted;
		this.deletedBy = deletedBy;
		this.deletedOn = deletedOn;
		this.createdOn = createdOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserManagement getUserId() {
		return userId;
	}

	public void setUserId(UserManagement userId) {
		this.userId = userId;
	}

	public UserManagement getFriendId() {
		return friendId;
	}

	public void setFriendId(UserManagement friendId) {
		this.friendId = friendId;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	
	
	
	
}
