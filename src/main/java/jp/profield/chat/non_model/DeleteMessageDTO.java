package jp.profield.chat.non_model;

public class DeleteMessageDTO {
	
	private String [] messageId;
	private String status;
	private String receiverId;
	private String senderId;
	
	
	public DeleteMessageDTO(String[] messageId, String status,
			String receiverId, String senderId) {
		super();
		this.messageId = messageId;
		this.status = status;
		this.receiverId = receiverId;
		this.senderId = senderId;
	}

	public DeleteMessageDTO() {
		super();
		
	}

	public String[] getMessageId() {
		return messageId;
	}

	public void setMessageId(String[] messageId) {
		this.messageId = messageId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	
}
