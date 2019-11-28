package jp.profield.chat.service.impl;

import jp.profield.chat.model.Group;
import jp.profield.chat.models.repository.GroupRepository;
import jp.profield.chat.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class GroupServiceImpl implements GroupService{

	@Autowired
	private GroupRepository groupRepository;

	@Override
	public Group saveGroup(Group group) {
		
		return groupRepository.save(group);
	}

	@Override
	public Group findByGroupIdAndActive(String groupId, boolean isDelete) {
		
		return groupRepository.findByGroupIdAndActive(groupId, isDelete);
	}
}
