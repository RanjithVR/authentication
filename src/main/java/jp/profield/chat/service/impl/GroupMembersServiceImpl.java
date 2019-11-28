package jp.profield.chat.service.impl;

import java.util.List;

import jp.profield.chat.model.GroupMembers;
import jp.profield.chat.models.repository.GroupMembersRepository;
import jp.profield.chat.service.GroupMembersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMembersServiceImpl implements GroupMembersService{

	@Autowired
	private GroupMembersRepository groupMembersRepository;

	@Override
	public GroupMembers saveGroupMemberToGroup(GroupMembers groupMembers) {
		
		return groupMembersRepository.save(groupMembers);
	}

	@Override
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupId(String groupId) {
		
		return groupMembersRepository.getAllActiveMemberOfGrpByGroupId(groupId);
	}

	@Override
	public GroupMembers getCurrentUserGrpInfoByUserIdAndGrpId(String groupId,
			String userId) {
		
		return groupMembersRepository.getCurrentUserGrpInfoByUserIdAndGrpId(groupId, userId);
	}

	@Override
	public List<GroupMembers> getAllGrpMembersIdByGroupId(String groupId) {
		
		return groupMembersRepository.getAllGrpMembersIdByGroupId(groupId, false);
	}

	@Override
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupIdForMessagePush(
			String groupId) {
	
		return groupMembersRepository.getAllActiveMemberOfGrpByGroupIdForMessagePush(groupId);
	}

	@Override
	public List<GroupMembers> findAllGroupsByActiveUserId(String userId,
			boolean isDelete) {
		return groupMembersRepository.findAllGroupsByUserId(userId, isDelete);
	}

	@Override
	public List<GroupMembers> findAnyAdminExistByGroupId(String groupId,
			int role) {

		return groupMembersRepository.findAnyAdminExistByGroupId(groupId, role);
	}

	
}
