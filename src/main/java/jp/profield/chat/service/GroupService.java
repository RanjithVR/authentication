package jp.profield.chat.service;

import jp.profield.chat.model.Group;

public interface GroupService {

	Group saveGroup(Group group);
	
	Group findByGroupIdAndActive(String groupId,boolean isDelete);
	
	
}
