package jp.profield.chat.web.rest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jp.profield.chat.model.IndividualChatMessage;

public class IndividualChatDTO {

	private String id;

	private String messageContent;

	private String receiverId;

	private String senderId;

	private boolean isRead;

	private LocalDateTime createdDate;

	private boolean isAttachment;

	private String attachemnt;

	private long senderUserUkId;
	
	private LocalDate date;
	private LocalTime time;
	
	private String attachmetExt;
	
	private String timeOfMessage;
	
	private boolean isDelete;

	public IndividualChatDTO() {
		super();
	}

	public IndividualChatDTO(IndividualChatMessage individualChatMessage) {
		super();
		this.id = individualChatMessage.getId();
		this.messageContent = individualChatMessage.getMessageContent();
		this.receiverId = individualChatMessage.getReceiverId();
		this.senderId = individualChatMessage.getSenderId();
		this.isRead = individualChatMessage.isRead();
		this.createdDate = individualChatMessage.getCreatedDate();
		this.isAttachment = individualChatMessage.getIsAttachment();
		this.attachemnt = individualChatMessage.getIsAttachment()==true ? individualChatMessage.getAttachemnt().getId():"";
		this.senderUserUkId = individualChatMessage.getSenderUserUkId();
		this.date =individualChatMessage.getCreatedDate().toLocalDate();
		this.time =individualChatMessage.getCreatedDate().toLocalTime();
		this.attachmetExt=individualChatMessage.getIsAttachment()==true ? individualChatMessage.getAttachemnt().getExtension():"";
		this.timeOfMessage=individualChatMessage.getCreatedDate().toLocalTime().getHour()+":"+
		individualChatMessage.getCreatedDate().toLocalTime().getMinute();
		this.isDelete=individualChatMessage.getIsDelete();
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

	public boolean isAttachment() {
		return isAttachment;
	}

	public void setAttachment(boolean isAttachment) {
		this.isAttachment = isAttachment;
	}

	public String getAttachemnt() {
		return attachemnt;
	}

	public void setAttachemnt(String attachemnt) {
		this.attachemnt = attachemnt;
	}

	public long getSenderUserUkId() {
		return senderUserUkId;
	}

	public void setSenderUserUkId(long senderUserUkId) {
		this.senderUserUkId = senderUserUkId;
	}
	
	public String getAttachmetExt() {
		return attachmetExt;
	}

	public void setAttachmetExt(String attachmetExt) {
		this.attachmetExt = attachmetExt;
	}
	
	

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	

	public String getTimeOfMessage() {
		return timeOfMessage;
	}

	public void setTimeOfMessage(String timeOfMessage) {
		this.timeOfMessage = timeOfMessage;
	}

	
	
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "IndividualChatDTO [id=" + id + ", messageContent="
				+ messageContent + ", receiverId=" + receiverId + ", senderId="
				+ senderId + ", isRead=" + isRead + ", createdDate="
				+ createdDate + ", isAttachment=" + isAttachment
				+ ", attachemnt=" + attachemnt + ", senderUserUkId="
				+ senderUserUkId + ", date=" + date + ", time=" + time
				+ ", attachmetExt=" + attachmetExt + ", timeOfMessage="
				+ timeOfMessage + "]";
	}

	

}
