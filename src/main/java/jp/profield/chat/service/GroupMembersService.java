package jp.profield.chat.service;

import java.util.List;

import jp.profield.chat.model.GroupMembers;

public interface GroupMembersService {

	GroupMembers saveGroupMemberToGroup(GroupMembers groupMembers);
	
	List<GroupMembers> getAllActiveMemberOfGrpByGroupId(String groupId);
	
	GroupMembers getCurrentUserGrpInfoByUserIdAndGrpId(String groupId,String userId);
	
	
	List<GroupMembers> getAllGrpMembersIdByGroupId(String groupId);
	
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupIdForMessagePush(String groupId);
	
	List<GroupMembers> findAllGroupsByActiveUserId(String userId, boolean isDelete);
	
	
	List<GroupMembers> findAnyAdminExistByGroupId(String groupId, int role);// role 2 is Admin
	
}
