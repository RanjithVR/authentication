package jp.profield.chat.non_model;

public class FriendRequestNotification {
	
	private String status;
	private long receiverUK;
	private String receiverId;
	public FriendRequestNotification() {
		super();
		
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getReceiverUK() {
		return receiverUK;
	}
	public void setReceiverUK(long receiverUK) {
		this.receiverUK = receiverUK;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	

}
