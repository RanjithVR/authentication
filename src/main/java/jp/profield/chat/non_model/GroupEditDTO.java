package jp.profield.chat.non_model;

import java.util.List;

import jp.profield.chat.model.Group;
import jp.profield.chat.model.GroupMembers;

public class GroupEditDTO {

	
	private Group group;
	private List<GroupMembers> groupMembersList;
	
	private GroupMembers currentUserGrpInfo;
	
	
	public GroupEditDTO() {
		super();
	}

	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

	public List<GroupMembers> getGroupMembersList() {
		return groupMembersList;
	}

	public void setGroupMembersList(List<GroupMembers> groupMembersList) {
		this.groupMembersList = groupMembersList;
	}

	public GroupMembers getCurrentUserGrpInfo() {
		return currentUserGrpInfo;
	}

	public void setCurrentUserGrpInfo(GroupMembers currentUserGrpInfo) {
		this.currentUserGrpInfo = currentUserGrpInfo;
	}
	
	
	
}
