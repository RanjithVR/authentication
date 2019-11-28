package jp.profield.chat.non_model;

import java.io.Serializable;
import java.util.Arrays;

public class GroupDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3243870321571682225L;
	
	private String groupName;
	private String discription;
	private String [] selectedUsers;
	public GroupDTO(String groupName, String discription, String[] selectedUsers) {
		super();
		this.groupName = groupName;
		this.discription = discription;
		this.selectedUsers = selectedUsers;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public String[] getselectedUsers() {
		return selectedUsers;
	}
	public void setselectedUsers(String[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((discription == null) ? 0 : discription.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + Arrays.hashCode(selectedUsers);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupDTO other = (GroupDTO) obj;
		if (discription == null) {
			if (other.discription != null)
				return false;
		} else if (!discription.equals(other.discription))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (!Arrays.equals(selectedUsers, other.selectedUsers))
			return false;
		return true;
	}
	
	
	
	
}
