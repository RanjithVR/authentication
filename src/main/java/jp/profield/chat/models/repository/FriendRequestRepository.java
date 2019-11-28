package jp.profield.chat.models.repository;

import java.util.List;
import java.util.Optional;

import jp.profield.chat.model.FriendRequest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FriendRequestRepository  extends MongoRepository<FriendRequest, String> {
	
	@Query("{ 'requested_to': ?0, 'reqByUserId.id': ?1, 'actedOrNot': ?2}")  //requested_by
	public Optional<FriendRequest> findByInviteeIdAndUserId(String inviteeId,String userId,boolean approveNot);

	@Query("{ 'requested_to': ?0, 'actedOrNot': ?1}")
	public List<FriendRequest> getAllPendingFriendRequestByUserId(
			String userId, boolean approveNot);
	
	@Query("{ 'requested_to': ?0, 'reqByUserId.id': ?1, 'actedOrNot': ?2}")
	public FriendRequest findFriendRequestByuserIdAndFriendId(String userId,String friendId, boolean approveNot);
	
	@Query("{'reqByUserId.id': ?0, 'reqByUserId.email':?1,'acted_on': ?2}")
	public List<FriendRequest> getAllPendingFriendRequestByUserIdForSearch(
			String userId, String emailId, boolean approveNot);
	
	@Query("{ 'reqByUserId.id': ?0, ' requested_to': ?1, 'actedOrNot': ?2}")
	public FriendRequest findRequestSendOrNotByUserIdAndFriendId(String userId,
			String friendId, boolean approveNot);
	
	
}
