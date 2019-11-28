package jp.profield.chat.web.rest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jp.profield.chat.model.Attachment;
import jp.profield.chat.model.GroupMessage;
public class GroupMessageDTO {

	

	private String id;
	private String messageContent;
	private String groupId;
	private String senderId;
	private boolean isRead;
	private boolean isDelete;
	private String senderName;
	private LocalDateTime createdDate;
	
	private LocalDate date;
	private LocalTime time;
	private String timeOfMessage;
	
	private Attachment attachemnt;
	private boolean isAttachment;
	private long groupUkId;
	
	private int messageType;

	
	
	public GroupMessageDTO() {
		super();
	}

	public GroupMessageDTO(GroupMessage groupMessage) {
		super();
		this.id = groupMessage.getId();
		this.messageContent = groupMessage.getMessageContent();
		this.groupId = groupMessage.getGroupId();
		this.senderId = groupMessage.getSenderId();
		this.isRead = groupMessage.getIsRead();
		this.isDelete = groupMessage.getIsDelete();
		this.senderName = groupMessage.getSenderName();
		this.createdDate = groupMessage.getCreatedDate();
		this.date = groupMessage.getCreatedDate().toLocalDate();;
		this.time = groupMessage.getCreatedDate().toLocalTime();
		this.timeOfMessage = groupMessage.getCreatedDate().toLocalTime().getHour()+":"+
				groupMessage.getCreatedDate().toLocalTime().getMinute();
		this.attachemnt=groupMessage.getAttachemnt();
		this.isAttachment=groupMessage.getIsAttachment();
		this.messageType=groupMessage.getMessageType();
		
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

	public Attachment getAttachemnt() {
		return attachemnt;
	}

	public void setAttachemnt(Attachment attachemnt) {
		this.attachemnt = attachemnt;
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
