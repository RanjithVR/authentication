package jp.profield.chat.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "IndividualChatMessage")
public class IndividualChatMessage {
	
	
	@Id
	private String id;
	

	@Field
	private String messageContent;
	

	@Field
	private String receiverId;
	

	@Field
	private String senderId;
	
	
	@Field
	private boolean isRead;
	
	
	@Field
	private LocalDateTime createdDate;

	@Field
	private boolean isAttachment;
	
	
	@DBRef
	private Attachment attachemnt;
	
	@Field
	private long senderUserUkId;
	
	@Field
	private boolean isDelete;
	
	@Field("message_type")
	private int messageType;
	
	
	public IndividualChatMessage() {
		super();
	}




	public IndividualChatMessage(String id, String messageContent,
			String receiverId, String senderId, boolean isRead,
			LocalDateTime createdDate, boolean isAttachment,
			Attachment attachemnt, long senderUserUkId, boolean isDelete,int messageType) {
		super();
		this.id = id;
		this.messageContent = messageContent;
		this.receiverId = receiverId;
		this.senderId = senderId;
		this.isRead = isRead;
		this.createdDate = createdDate;
		this.isAttachment = isAttachment;
		this.attachemnt = attachemnt;
		this.senderUserUkId = senderUserUkId;
		this.isDelete = isDelete;
		this.messageType=messageType;
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


	public String getReceiverId() {
		return receiverId;
	}


	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}


	public String getSenderId() {
		return senderId;
	}


	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public boolean isRead() {
		return isRead;
	}


	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}



	public LocalDateTime getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public boolean getIsDelete() {
		return isDelete;
	}



	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}




	public boolean getIsAttachment() {
		return isAttachment;
	}


	public void setIsAttachment(boolean isAttachment) {
		this.isAttachment = isAttachment;
	}


	public Attachment getAttachemnt() {
		return attachemnt;
	}


	public void setAttachemnt(Attachment attachemnt) {
		this.attachemnt = attachemnt;
	}


	public long getSenderUserUkId() {
		return senderUserUkId;
	}


	public void setSenderUserUkId(long senderUserUkId) {
		this.senderUserUkId = senderUserUkId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	
	
}
