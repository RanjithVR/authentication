package jp.profield.chat.non_model;

public class DeleteMessage {
	
	private String status;
	private String [] messageid;

	public DeleteMessage() {
	
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String[] getMessageid() {
		return messageid;
	}
	public void setMessageid(String[] messageid) {
		this.messageid = messageid;
	}
	
	

}
