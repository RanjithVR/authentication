package jp.profield.chat.models.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import jp.profield.chat.model.IndividualChatMessage;

public interface IndividualChatMessageRepository extends MongoRepository<IndividualChatMessage, String> {

	 @Query(value = "{$or: [ { 'receiverId' : ?0, 'senderId' : ?1 }, { 'receiverId' : ?1, 'senderId' : ?0 } ]}")
	List<IndividualChatMessage> findAllByReceiverIdAndSenderId(String receiverId, String senderId);
	 
	 @Query(value = "{$or: [ { 'receiverId' : ?0 }, {  'senderId' : ?0 } ]}")
	 List<IndividualChatMessage> findAllMessgeForContactList(String receiverId);
	 
	 @Query(value = "{ _id : { $in :?0  } } ")
	 List<IndividualChatMessage> findAllMessageByMessageIds(String[] messageIds);
	
	
}
