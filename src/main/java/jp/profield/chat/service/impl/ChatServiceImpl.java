package jp.profield.chat.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.profield.chat.WebscoketBean;
import jp.profield.chat.api.dto.FriendRequestDTO;
import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.FriendRequest;
import jp.profield.chat.model.Group;
import jp.profield.chat.model.GroupMembers;
import jp.profield.chat.model.GroupMessage;
import jp.profield.chat.model.IndividualChatMessage;
import jp.profield.chat.model.MemberInvitation;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.GroupMessageRepository;
import jp.profield.chat.models.repository.IndividualChatMessageRepository;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.non_model.DeleteMessageDTO;
import jp.profield.chat.non_model.FriendRequestNotification;
import jp.profield.chat.non_model.UserExistStatus;
import jp.profield.chat.non_model.MessageContentDTO;
import jp.profield.chat.non_model.MessageScoketDTO;
import jp.profield.chat.non_model.Status;
import jp.profield.chat.non_model.UserInviteDTO;
import jp.profield.chat.service.ChatService;
import jp.profield.chat.service.ContactListService;
import jp.profield.chat.service.FriendRequestService;
import jp.profield.chat.service.GroupMembersService;
import jp.profield.chat.service.GroupService;
import jp.profield.chat.service.MailSendService;
import jp.profield.chat.service.MemberInvitationService;
import jp.profield.chat.service.UserManagementService;
import jp.profield.chat.web.rest.dto.GroupMessageDTO;
import jp.profield.chat.web.rest.dto.IndividualChatDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
@Service("chatService")
public class ChatServiceImpl implements ChatService {
	@Autowired
	private UserManagementService userManagementService ;
	@Autowired
	private MemberInvitationService memberInvitationService;
	@Autowired
	private FriendRequestService friendRequestService;
	@Autowired
	private ContactListService contactListService;
	@Autowired
	private IndividualChatMessageRepository individualChatMessageRepository;
	@Autowired
	private GroupMessageRepository groupMessageRepository;
	@Autowired
	private GroupMembersService groupMembersService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MailSendService mailSendService;

	@Override
	public UserInviteDTO inviteNewUserByMail(String emailId, UserManagement user) {

		UserInviteDTO userInvite = new UserInviteDTO();

		Optional<UserManagement> userExist = userManagementService
				.findByEmailAndActivated(emailId, false);
		if (!userExist.isPresent()) {

			Optional<MemberInvitation> checkStatus = memberInvitationService
					.findByEmailAndUserId(emailId, user.getId());
			if (!checkStatus.isPresent()) {

				MemberInvitation memberInvitaion = new MemberInvitation();
				memberInvitaion.setEmail(emailId);
				memberInvitaion.setUser(user);
				memberInvitaion.setInvitedOn(new Date());
				memberInvitationService.saveMemberInvitation(memberInvitaion);
				String htmlMsg="";
				mailSendService.sendingInvitationToNewUser(emailId,
						"Invitation to join Profield Chat!!!", htmlMsg, user.getFirstName() +" "+user.getLastName());
			
				userInvite.setStatus("sucess");
			} else {
				
				
				userInvite.setStatus("alredy_send");
				// contact list  checking ..!!!
			}

		} else {
			ContactList contactList=contactListService.
					findContactListExsistByUserIdAndFrndId(user.getId(), userExist.get().getId());
			
			
			if(contactList!=null){
				userInvite.setStatus("alredy_frnd");
				userInvite.setUser(userExist.get());
				userInvite.setAlreadyFrnd(true);
			}else{
				
				Optional<FriendRequest> requestExist = friendRequestService
						.findByInviteeIdAndUserId( userExist.get().getId(),user.getId(),false);
				
				if (requestExist.isPresent()) {
					userInvite.setStatus("request_send");// alredy  request send
					userInvite.setUser(userExist.get());
				}else{
					userInvite.setStatus("user_found");
					userInvite.setUser(userExist.get());
				}
				
				
				
			}
			
		}

		return userInvite;
	}


	@Override
	public Status sendFriendRequest(String invitee_id,
			UserManagement userManagement) {

		Status status = new Status();
		Optional<FriendRequest> requestExist = friendRequestService
				.findByInviteeIdAndUserId(invitee_id, userManagement.getId(),false);
		
		if (!requestExist.isPresent()) {
			
			Optional<UserManagement> frndObj=userRepository.findById(invitee_id);

			FriendRequest request = new FriendRequest();
			request.setReqByUserId(userManagement);
			request.setReqToUserId(invitee_id);
			Date date = new Date();
			request.setRequestOn(date);
			request.setActedOrNot(false);
			friendRequestService.saveFriendRequest(request);
			status.setMessage("sucess");
			
			//live notification  to friend 
			
			if(frndObj.isPresent()){

				WebSocketSession friendSockt = (WebSocketSession) WebscoketBean
						.getmLiveSession().get(invitee_id);
				if (friendSockt != null) {
					
					
					FriendRequestNotification frqNotifcation = new FriendRequestNotification();
					
					frqNotifcation.setStatus("new_frnd_Req");
					frqNotifcation.setReceiverUK(frndObj.get().getUkMessageKey());
					frqNotifcation.setReceiverId(frndObj.get().getId());

					
					Gson gsonObj = new Gson();
					gsonObj.toJson(frqNotifcation);
					try {
						friendSockt.sendMessage(new TextMessage(gsonObj
								.toJson(frqNotifcation)));
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
				
			}
			
		} else {
			status.setMessage("alreadySend");
		}

		return status;
	}


	@Override
	public IndividualChatDTO sendTextMessage(MessageContentDTO messageContentDTO) {
		
		Optional<UserManagement> senderUser =  userManagementService.findOneByid(messageContentDTO.getSenderUserId());

		Optional<UserManagement> opuser = userManagementService.findOneByid(messageContentDTO.getReceiveUserId());
		
		IndividualChatMessage messageObj = new IndividualChatMessage();
		List<IndividualChatMessage> messageList= new ArrayList<IndividualChatMessage>();
		List<IndividualChatDTO> result=new ArrayList<IndividualChatDTO>();
		
		if (opuser.isPresent()) {

			messageObj.setSenderId(senderUser.get().getId());
			messageObj.setMessageContent(messageContentDTO.getMessage());
			messageObj.setReceiverId(opuser.get().getId());
			messageObj.setSenderId(senderUser.get().getId());
			messageObj.setRead(true);
			
			LocalDateTime now = LocalDateTime.now();  
			messageObj.setCreatedDate(now);
			messageObj = individualChatMessageRepository.save(messageObj);
			messageObj.setSenderUserUkId(senderUser.get().getUkMessageKey());
			messageList.add(messageObj);
			result=messageList.stream().map(IndividualChatDTO::new).collect(Collectors.toList());
		}
		
		return result.get(0);
	}


	@Override
	public GroupMessageDTO sendGroupTextMessage(
			MessageContentDTO messageContentDTO) {
		

		GroupMessage grpMessage = new GroupMessage();
		List<GroupMessage> messageList= new ArrayList<GroupMessage>();
		List<GroupMessageDTO> result=new ArrayList<GroupMessageDTO>();
		
		Optional<UserManagement> senderUser =  userManagementService.findOneByid(messageContentDTO.getSenderUserId());
		if(senderUser.isPresent()){
			
			Group group= groupService.findByGroupIdAndActive(messageContentDTO.getReceiveUserId(), false);
			if(group!=null){
			
			//messageDTO.setSenderName(senderUser.get().getFirstName());
			grpMessage.setIsDelete(false);
			grpMessage.setGroupId(messageContentDTO.getReceiveUserId());
			grpMessage.setMessageContent(messageContentDTO.getMessage());
			grpMessage.setIsRead(true);
			grpMessage.setSenderId(senderUser.get().getId());
			
			grpMessage.setSenderName(senderUser.get().getFirstName() +" "+senderUser.get().getLastName());
			 LocalDateTime now = LocalDateTime.now();  
			grpMessage.setCreatedDate(now);
			groupMessageRepository.save(grpMessage);
			
			grpMessage.setGroupUkId(group.getGroupUniqeId());
			messageList.add(grpMessage);
			result=messageList.stream().map(GroupMessageDTO::new).collect(Collectors.toList());
			
			}
		}
		
		return result.get(0);
	}


	@Override
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupIdForMessagePush(
			String groupId) {
		
		List<GroupMembers> groupMembersList=groupMembersService.getAllActiveMemberOfGrpByGroupIdForMessagePush(groupId);
		
		return groupMembersList;
	}


	@Override
	public Status forgotPassword(String emailId, String userName) {
		
		Status status= new Status();
	
		Optional<UserManagement> user=userRepository.findByEmailAndActivated(emailId, false);
		if(user.isPresent()){
			
			UserManagement userObj=user.get();
			if(userObj.getUserName().equalsIgnoreCase(userName)){
				long token=Math.round(Math.random() * 100000);
				
				mailSendService.sendTokenForForgotPassword(user.get().getFirstName() +" "+user.get().getLastName(),
						               user.get().getEmail(), token);
				
				userObj.setToken(token);
				userObj.setResetpwdLinkGeneratedOn(new Date());
				userRepository.save(userObj);
				status.setMessage("send");
			}else{
				status.setMessage("not-found");
			}
			
		}else{
			status.setMessage("not-found");
		}
		
		return status;
	}


	@Override
	public Status savePasswordFromForgotPassword(long tocken, String password,String emailId) {
		
		Status status= new Status();
		Optional<UserManagement> user=userRepository.findByEmailAndActivated(emailId, false);
		if(user.isPresent()){
			
			if(user.get().getToken()==tocken){
				UserManagement userObj=user.get();
				 userObj.setPassword((passwordEncoder.encode(password)));
				 userRepository.save(userObj);
				 
				 status.setMessage("sucess");
				
			}else{
				status.setMessage("invalid-tocken");
			}
			
		}else{
			status.setMessage("userNotfound");
		}
		
		return status;
	}


	@Override
	public Status userSelfDeRegistration(UserManagement user) {
		Status status= new Status();
		if(user!=null){
			//Check this user is an Owner of any group, if so move the ownership to next Admin or User
			List<GroupMembers> grpMembersList= groupMembersService.findAllGroupsByActiveUserId(user.getId(), false);
			
			if(grpMembersList!=null){
				
				for(GroupMembers grpMemberObj:grpMembersList){
					//check the user status of each group
					GroupMembers isUserOwner= groupMembersService.
							getCurrentUserGrpInfoByUserIdAndGrpId(grpMemberObj.getGroup().getId(), user.getId());
					
					if(isUserOwner!=null){
						// if  the user is a owner of the group 
						if(isUserOwner.getGroupRole()==1){
							
							// check any Admin exist for transfer the Ower power. role admin is 2
							List<GroupMembers> adminMembersList=groupMembersService.
									findAnyAdminExistByGroupId(grpMemberObj.getGroup().getId(), 2);
							if(adminMembersList!=null && adminMembersList.size()>0){
								
							
								GroupMembers  adminMember=adminMembersList.get(0);
								adminMember.setGroupRole(1);
								groupMembersService.saveGroupMemberToGroup(adminMember);
								
							}else{
								//if no admin in the group Select the first Memebr from grpMember add power Admin & owner
								List<GroupMembers> groupMembersList=groupMembersService.
										findAnyAdminExistByGroupId(grpMemberObj.getGroup().getId(),3);
								if(groupMembersList.size()>0 && groupMembersList!=null){

									GroupMembers  adminMember=groupMembersList.get(0);
									adminMember.setGroupRole(1);
									groupMembersService.saveGroupMemberToGroup(adminMember);
								}
							
								
							}
							
						}
						
						// soft delete user from group members  collection
						isUserOwner.setIsDeleted(true);
						groupMembersService.saveGroupMemberToGroup(isUserOwner);
						
						
						String userLeftMessage = isUserOwner.getUser().getFirstName()+" "+isUserOwner.getUser().getLastName()+" left the group";
						String mesageSenderName = isUserOwner.getUser().getFirstName()+" "+isUserOwner.getUser().getLastName();
						//insert message as user left group
						GroupMessage groupMessage = new GroupMessage();
						groupMessage.setIsDelete(false);
						groupMessage.setGroupId(isUserOwner.getGroup().getId());
						groupMessage.setMessageContent(userLeftMessage);
						groupMessage.setIsRead(true);
						groupMessage.setSenderId(isUserOwner.getUser().getId());
						groupMessage.setSenderName(mesageSenderName);
						LocalDateTime now = LocalDateTime.now();
						groupMessage.setCreatedDate(now);
						groupMessage.setIsAttachment(false);
						groupMessageRepository.save(groupMessage);
						
						//inform to group members through socket
						List<GroupMembers> groupMemberList=groupMembersService.getAllActiveMemberOfGrpByGroupId(isUserOwner.getGroup().getId());
						for(GroupMembers memberObject : groupMemberList){
							
							WebSocketSession senderSocket = (WebSocketSession) WebscoketBean
									.getmLiveSession().get(memberObject.getUser().getId());
							if (senderSocket != null) {
															
								UserExistStatus groupExistStatus = new UserExistStatus();
								groupExistStatus.setGroupId(memberObject.getGroup().getId());
								groupExistStatus.setStatus("leftgroup");
								groupExistStatus.setGroupMessage(groupMessage);
								
								Gson gsonObj = new Gson();
								gsonObj.toJson(groupExistStatus);
								try {
									senderSocket.sendMessage(new TextMessage(gsonObj
											.toJson(groupExistStatus)));
								} catch (IOException e) {

									e.printStackTrace();
								}
							}
						}
					
						
						
					}
				}
				
			}
			
			//selecting user  contact list for soft delete
			List<ContactList> userContactList= contactListService.findAllContactListByUserId(user.getId());
			if(userContactList!=null){
				for(ContactList contact: userContactList){
					contact.setIsDeleted(true);
					contactListService.saveContactList(contact);
					
					//TODO gibin
					// For Online friends, send the last message as "This user is de-registered/removed, is not available
					
					
				}
			}
			
			
			
			
			
			// update user collection as deleted(set is delete as true) 
			user.setIsDeleted(true);
			userRepository.save(user);
			status.setMessage("de-registered");
			
		}
		
		return status;
	}


	@Override
	public List<FriendRequest> findAllPendingRequestByUserId(String userId,
			boolean isApprove) {
		
		
		List<FriendRequest> friendRequest= friendRequestService.
				getAllPendingFriendRequestByUserId(userId, false);
		
		
		return friendRequest;
	}


	@Override
	public Status approveFriendRequestByUserId(String friendUserId,UserManagement user) {
		
		Status status= new Status();
		
		FriendRequest friendRequest= friendRequestService.
				findFriendRequestByuserIdAndFriendId(user.getId(), friendUserId,false);
		 if(friendRequest!=null){

				Optional<UserManagement> inviterUser = userRepository.findById(friendUserId);
				if (inviterUser.isPresent()) {
					// save to current user Contact list
					ContactList contactList = new ContactList();
					contactList.setFriendId(inviterUser.get());
					contactList.setUserId(user);
					Date date = new Date();
					contactList.setCreatedOn(date);
					contactListService.saveContactList(contactList);

					// save to Friend Contact list
					ContactList contactListFrnd = new ContactList();

					contactListFrnd.setFriendId(user);
					contactListFrnd.setUserId(inviterUser.get());
					contactListFrnd.setCreatedOn(date);
					contactListService.saveContactList(contactListFrnd);
					
					//set friend request as processed
					friendRequest.setActedOrNot(true);
					friendRequestService.saveFriendRequest(friendRequest);
					
					status.setMessage("approve");
				}
		 }
		


		return status;
	}


	@Override
	public Status rejectFriendRequestByUserId(String friendUserId,
			UserManagement user) {
		
     Status status= new Status();
		
		FriendRequest friendRequest= friendRequestService.
				findFriendRequestByuserIdAndFriendId(user.getId(), friendUserId,false);
		
		
		 if(friendRequest!=null){
			 
			    //set friend request as processed 
				friendRequest.setActedOrNot(true);
				friendRequestService.saveFriendRequest(friendRequest);
				status.setMessage("rejected");
		 }
		
		return status;
	}


	@Override
	public List<ContactList> findAllOnlineUsersByUserId(String userId,boolean isOnline) {
	
		List<ContactList> userList=contactListService.
				findAllOnlineUserByUserId(userId,isOnline);
		
		if(userList!=null){
			
		}
		
		return userList;
	}


	@Override
	public void updateUserOnlineStatus(String userId, boolean isOnline) {
		
		Optional<UserManagement> currentUser = userRepository.findById(userId);
		if (currentUser.isPresent()) {
			UserManagement userObj=currentUser.get();
			Date date =new Date();
			if(isOnline){
				userObj.setIsOnline(isOnline);
				userObj.setLoginAt(date);
			}else{
				userObj.setIsOnline(isOnline);
				userObj.setLogoutAt(date);
			}
			
			userRepository.save(userObj);
		}
	}


	
	
}
