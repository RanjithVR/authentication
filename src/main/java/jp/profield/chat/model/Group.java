package jp.profield.chat.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "group")
public class Group {

	@Id
	private String id;

	@Field("group_name")
	private String groupName;
	
	@Field("description")
	private String description;
	
	@Field("profile_image")
	private byte[] profileImage;
	
	@DBRef
	private Attachment attachment;
	
	
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
	

	@Field("groupUniqeId")
	private Long groupUniqeId;
	

	public Group() {
		super();
	}


	public Group(String id, String groupName, String description,
			byte[] profileImage, Attachment attachment, boolean isDeleted,
			String deletedBy, Date deletedOn, String createdBy, Date createdOn,
			Long groupUniqeId) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.description = description;
		this.profileImage = profileImage;
		this.attachment = attachment;
		this.isDeleted = isDeleted;
		this.deletedBy = deletedBy;
		this.deletedOn = deletedOn;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.groupUniqeId = groupUniqeId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public byte[] getProfileImage() {
		return profileImage;
	}


	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}


	public Attachment getAttachment() {
		return attachment;
	}


	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
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


	public Long getGroupUniqeId() {
		return groupUniqeId;
	}


	public void setGroupUniqeId(Long groupUniqeId) {
		this.groupUniqeId = groupUniqeId;
	}

	
}
