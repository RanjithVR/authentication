package jp.profield.chat.models.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import jp.profield.chat.model.GroupMessage;

public interface GroupMessageRepository extends MongoRepository< GroupMessage, String> {


	List<GroupMessage> findAllBygroupId(String grpId);
}
