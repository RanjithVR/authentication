package jp.profield.chat.service.impl;

import java.util.List;
import java.util.Optional;

import jp.profield.chat.model.FriendRequest;
import jp.profield.chat.models.repository.FriendRequestRepository;
import jp.profield.chat.service.FriendRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestServiceImpl implements   FriendRequestService{

	@Autowired
	FriendRequestRepository friendRequestRepository;
	@Override
	public FriendRequest saveFriendRequest(FriendRequest friendRequest) {
		return friendRequestRepository.save(friendRequest);
	}
	@Override
	public Optional<FriendRequest> findByInviteeIdAndUserId(String inviteeId,
			String userId,boolean approveNot) {
		
		return friendRequestRepository.findByInviteeIdAndUserId(inviteeId, userId,approveNot);
	}
	@Override
	public List<FriendRequest> getAllPendingFriendRequestByUserId(
			String userId, boolean approveNot) {
		
		return friendRequestRepository.getAllPendingFriendRequestByUserId(userId, approveNot);
	}
	@Override
	public FriendRequest findFriendRequestByuserIdAndFriendId(String userId,
			String friendId, boolean approveNot) {
		
		return friendRequestRepository.findFriendRequestByuserIdAndFriendId(userId, friendId,  approveNot);
	}
	@Override
	public List<FriendRequest> getAllPendingFriendRequestByUserIdForSearch(
			String userId, String emailId, boolean approveNot) {
		
		return friendRequestRepository.getAllPendingFriendRequestByUserIdForSearch(userId, emailId, approveNot);
	}
	@Override
	public FriendRequest findRequestSendOrNotByUserIdAndFriendId(String userId,
			String friendId, boolean approveNot) {
		
		return friendRequestRepository.findRequestSendOrNotByUserIdAndFriendId(userId, friendId, approveNot);
	}

	
}
