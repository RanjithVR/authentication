package jp.profield.chat.non_model;

public class MessageContentDTO {

	private String message;
	
	private String receiveUserId;
	
	private String type;
	
	private String status;
	
	private String senderUserId;
	
	private String senderUK;
	
	

	public MessageContentDTO() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getSenderUK() {
		return senderUK;
	}

	public void setSenderUK(String senderUK) {
		this.senderUK = senderUK;
	}
	
	
	
	
	
}
