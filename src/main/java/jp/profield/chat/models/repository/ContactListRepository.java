package jp.profield.chat.models.repository;

import java.util.List;

import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.FriendRequest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ContactListRepository extends MongoRepository<ContactList, String> {

	@Query("{ 'userId.id': ?0, 'friendId.id': ?1}")
	public ContactList findContactListExsistByUserIdAndFrndId(String userId,
			String frndId);

	@Query("{ 'userId.id': ?0, 'is_deleted': ?1}")
	public List<ContactList> findAllActiveContactByUserId(String userId,boolean status);
	
	 @Query(value = "{'userId.id': ?0, user_id: { $nin:?1 } }")
	public List<ContactList> findAllContactNotIntheGroupbyUserId(String userId,String [] grpMemebrsId);
	 
	 @Query(value = "{$or: [ { 'userId.id' : ?0 }, { 'friendId.id' : ?0 } ]}") 
	public List<ContactList> findAllContactListByUserId(String userId);
	 
	 @Query(value = "{'userId.id': ?0}")
	 public List<ContactList> findAllOnlineUserByUserId(String userId,boolean isOnline);
	 
	@Query("{ 'userId.id': ?0, 'friendId.id': ?1,'is_deleted': ?2}")
	public ContactList findContactListExsistByUserIdAndFrndIdAndIsDeleted(
				String userId, String frndId, boolean isDeleted);

	
}
