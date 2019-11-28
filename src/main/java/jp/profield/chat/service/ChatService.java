package jp.profield.chat.service;

import java.util.List;

import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.FriendRequest;
import jp.profield.chat.model.GroupMembers;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.non_model.MessageContentDTO;
import jp.profield.chat.non_model.Status;
import jp.profield.chat.non_model.UserInviteDTO;
import jp.profield.chat.web.rest.dto.GroupMessageDTO;
import jp.profield.chat.web.rest.dto.IndividualChatDTO;

public interface ChatService {
	
	
	public UserInviteDTO inviteNewUserByMail(String emailId, UserManagement user);
	
	public Status sendFriendRequest(String invitee_id,UserManagement userManagement);
	
	public IndividualChatDTO sendTextMessage(MessageContentDTO messageContentDTO) ;
	
	public GroupMessageDTO sendGroupTextMessage(MessageContentDTO messageContentDTO);
	
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupIdForMessagePush(String groupId);
	
	public Status forgotPassword(String emailId,String userName);
	
	public Status savePasswordFromForgotPassword(long tocken,String password,String emailId);
	
	public Status userSelfDeRegistration(UserManagement user);
	
	 public List<FriendRequest> findAllPendingRequestByUserId(String userId,boolean isApprove);
	 
	public Status approveFriendRequestByUserId(String friendUserId,UserManagement user);
	
	public Status rejectFriendRequestByUserId(String friendUserId,UserManagement user);
	
	public List<ContactList> findAllOnlineUsersByUserId(String userId,boolean isOnline);
	
	public void updateUserOnlineStatus(String userId,boolean isOnline);
	

	

}
