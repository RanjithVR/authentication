package jp.profield.chat.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Group-Mesage")
public class GroupMessage {

	
	
	@Id
	private String id;
	

	@Field
	private String messageContent;
	
	@Field
	private String groupId;
	
	@Field
	private String senderId;
	
	
	@Field
	private boolean isRead;
	
	
	@Field
	private boolean isDelete;
	
	@Field
	private String senderName;
	
	
	@Field
	private LocalDateTime createdDate;
	
	@DBRef
	private Attachment attachemnt;
	
	@Field
	private boolean isAttachment;
	
	@Transient
	private long groupUkId;
	
	@Field("message_type")
	private int messageType;


	public GroupMessage() {
		super();
		
	}


	public GroupMessage(String id, String messageContent, String groupId,
			String senderId, boolean isRead, boolean isDelete,
			String senderName, LocalDateTime createdDate,
			Attachment attachemnt, boolean isAttachment,int messageType) {
		super();
		this.id = id;
		this.messageContent = messageContent;
		this.groupId = groupId;
		this.senderId = senderId;
		this.isRead = isRead;
		this.isDelete = isDelete;
		this.senderName = senderName;
		this.createdDate = createdDate;
		this.attachemnt = attachemnt;
		this.isAttachment = isAttachment;
		this.messageType=messageType;
	}



	public Attachment getAttachemnt() {
		return attachemnt;
	}

	public void setAttachemnt(Attachment attachemnt) {
		this.attachemnt = attachemnt;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getMessageContent() {
		return messageContent;
	}


	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getSenderId() {
		return senderId;
	}


	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public boolean getIsRead() {
		return isRead;
	}


	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}


	public boolean getIsDelete() {
		return isDelete;
	}


	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}


	public String getSenderName() {
		return senderName;
	}
	

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}


	public LocalDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public boolean getIsAttachment() {
		return isAttachment;
	}

	public void setIsAttachment(boolean isAttachment) {
		this.isAttachment = isAttachment;
	}


	public long getGroupUkId() {
		return groupUkId;
	}


	public void setGroupUkId(long groupUkId) {
		this.groupUkId = groupUkId;
	}


	public int getMessageType() {
		return messageType;
	}


	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	
	
	
}
