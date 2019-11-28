package jp.profield.chat.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "group_member")
public class GroupMembers {
	
	@Id
	private String id;
	
	@DBRef
	private Group group;
	
	@DBRef
	private UserManagement user;
	
	@Field("group_role")
	private int groupRole;
	
	@Field("is_member")
	private boolean isMember;
	

	@Field("is_deleted")
	private boolean isDeleted = false;


	@Field("deleted_by")
	private String deletedBy;

	@Field("deleted_on")
	private Date deletedOn;

	@Field("created_by")
	private String createdBy;

	@Field("created_on")
	private Date createdOn;

	public GroupMembers() {
		super();
	}

	public GroupMembers(String id, Group group, UserManagement user,
			int groupRole, boolean isMember, boolean isDeleted,
			String deletedBy, Date deletedOn, String createdBy, Date createdOn) {
		super();
		this.id = id;
		this.group = group;
		this.user = user;
		this.groupRole = groupRole;
		this.isMember = isMember;
		this.isDeleted = isDeleted;
		this.deletedBy = deletedBy;
		this.deletedOn = deletedOn;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public UserManagement getUser() {
		return user;
	}

	public void setUser(UserManagement user) {
		this.user = user;
	}

	public int getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(int groupRole) {
		this.groupRole = groupRole;
	}

	public boolean isMember() {
		return isMember;
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsDeletedBy() {
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	
	

}
