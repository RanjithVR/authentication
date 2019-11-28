package jp.profield.chat.service.impl;

import jp.profield.chat.models.repository.GroupMessageRepository;
import jp.profield.chat.service.GroupMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMessageServiceImpl implements GroupMessageService{
	
	@Autowired
	private GroupMessageRepository  groupMessageRepository;

}
