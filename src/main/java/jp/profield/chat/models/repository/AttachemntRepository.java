package jp.profield.chat.models.repository;

import jp.profield.chat.model.Attachment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttachemntRepository extends MongoRepository<Attachment, String> {

	
}
