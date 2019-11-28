package jp.profield.chat.service;

import java.util.List;
import java.util.Optional;

import jp.profield.chat.model.FriendRequest;

public interface FriendRequestService {

	FriendRequest saveFriendRequest(FriendRequest friendRequest);
	
	 Optional<FriendRequest> findByInviteeIdAndUserId(String inviteeId,String userId,boolean approveNot);
	 
	 List<FriendRequest> getAllPendingFriendRequestByUserId(String userId,boolean approveNot);
	 
	 FriendRequest findFriendRequestByuserIdAndFriendId(String userId,String friendId, boolean approveNot);
	 
	 
	 List<FriendRequest> getAllPendingFriendRequestByUserIdForSearch(String userId,String emailId,boolean approveNot);
	 
	 FriendRequest findRequestSendOrNotByUserIdAndFriendId(String userId,String friendId, boolean approveNot);
	 
	 
}
