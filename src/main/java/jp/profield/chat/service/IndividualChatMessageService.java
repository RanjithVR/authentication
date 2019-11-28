package jp.profield.chat.service;

import java.util.List;

import jp.profield.chat.model.IndividualChatMessage;

public interface IndividualChatMessageService {
	
	public List<IndividualChatMessage> findAllMessageByMessageIds(String [] messageIds);
	
	public IndividualChatMessage saveMessage(IndividualChatMessage message);

}
