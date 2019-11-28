package jp.profield.chat.non_model;

import java.io.Serializable;

/**
 * A DTO for the Message entity.
 * 
 * @author Ranjith
 * @since 27-09-2018
 * @version 1.0
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private long receiveUserId;
	private long senderUserId;
	private String type;
	private String senderName;
	private String groupId;
	private String senderId;
	private String status;
	private long senderUK;
	
	

	public Message() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public long getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(long senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
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
	

}
