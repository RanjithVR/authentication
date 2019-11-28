package jp.profield.chat.non_model;

import java.io.Serializable;
import java.util.List;


public class APIRequest<T> implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private String authKey;
	private T body;
	private List<T> listBody;
	private String description;

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<T> getListBody() {
		return listBody;
	}

	public void setListBody(List<T> listBody) {
		this.listBody = listBody;
	}

}
