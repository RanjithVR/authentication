package jp.profield.chat.service.impl;

import java.util.List;

import jp.profield.chat.model.IndividualChatMessage;
import jp.profield.chat.models.repository.IndividualChatMessageRepository;
import jp.profield.chat.service.IndividualChatMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndividualChatMessageServiceImpl implements IndividualChatMessageService {

	@Autowired
	private IndividualChatMessageRepository individualChatMessageRepository;
	
	@Override
	public List<IndividualChatMessage> findAllMessageByMessageIds(
			String[] messageIds) {
		return individualChatMessageRepository.findAllMessageByMessageIds(messageIds);
	}

	@Override
	public IndividualChatMessage saveMessage(IndividualChatMessage message) {
	
		return individualChatMessageRepository.save(message);
	}

}
