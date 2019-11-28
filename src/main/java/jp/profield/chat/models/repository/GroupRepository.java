package jp.profield.chat.models.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import jp.profield.chat.model.Group;

public interface GroupRepository extends MongoRepository<Group, String> {

	
	
	Group findByGroupName(String groupName);
	
	@Query("{ 'id': ?0, 'is_deleted': ?1}")
	public Group findByGroupIdAndActive(String groupId, boolean isDelete);
}
