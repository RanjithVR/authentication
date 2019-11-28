package jp.profield.chat.spring.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import jp.profield.chat.WebscoketBean;
import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.GroupMembers;
import jp.profield.chat.non_model.MessageContentDTO;
import jp.profield.chat.non_model.MessageScoketDTO;
import jp.profield.chat.web.rest.dto.GroupMessageDTO;
import jp.profield.chat.web.rest.dto.IndividualChatDTO;

import org.json.JSONObject;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class WebSocketHandler extends AbstractWebSocketHandler{


	
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
	    	MessageScoketDTO messageDTOObj= new MessageScoketDTO();
	    	JSONObject jsonObject = new JSONObject(message.getPayload());
	    	
	    	String status=jsonObject.get("status").toString();
	    	if(status.equalsIgnoreCase("connect")){
	    	 	  String userId=jsonObject.get("message").toString();
	    	 	  
	    	 	 //add to Map using  user  id 
	    	 	 WebscoketBean.getmLiveSession().put(userId, session);
	    	 	// mLiveSession.put(userId, session);
	    	 	 
	    	 	// inform all friends to  user is Online
	    	 	List<ContactList> onlineMembersList = DemoBeanUtil
						.getChatService().findAllOnlineUsersByUserId(userId,true);
				
				if(onlineMembersList!=null){
					
					messageDTOObj= new MessageScoketDTO();
					messageDTOObj.setStatus("onlineStatus");
					messageDTOObj.setOnlineStatus(true);// is online
					messageDTOObj.setSenderUserId(userId);
					Gson gsonObj = new Gson();
	    		    gsonObj.toJson(messageDTOObj);
	    		    for(ContactList friendList:onlineMembersList){
	    		    	
	    		    	// WebscoketBean.getmLiveSession()
	    		    	WebSocketSession senderSocket= (WebSocketSession) WebscoketBean.getmLiveSession().get(friendList.getFriendId().getId());
	    		    	// WebSocketSession senderSocket=(WebSocketSession)mLiveSession.get(friendList.getFriendId().getId());
	   	    			   if(senderSocket!=null){
	   	    				 senderSocket.sendMessage(new TextMessage( gsonObj.toJson(messageDTOObj)));
	   	    			   }
	    		    }
				}
	    	 	 
	    	}else if(status.equalsIgnoreCase("message_ind")){
	    			
	    			Gson gson = new Gson();
	    			MessageContentDTO messageContentDTO = gson.fromJson(message.getPayload(), MessageContentDTO.class);
	    			IndividualChatDTO messageObj=DemoBeanUtil.getChatService().sendTextMessage(messageContentDTO);
	    		
			if (messageObj != null) {
				messageDTOObj.setIndividualChatDTO(messageObj);
				messageDTOObj.setStatus("chat");
				// JSONObject jsonObj = new JSONObject(messageDTOObj);

				gson.toJson(messageDTOObj);
				System.out.println(messageContentDTO.getMessage()  +"==Received=="+messageContentDTO.getReceiveUserId()  +"==sender=="+messageContentDTO.getSenderUserId());
				
				// find the receiver socket and push message
				WebSocketSession mEndSocket= (WebSocketSession) WebscoketBean.getmLiveSession().get(messageContentDTO.getReceiveUserId());
    		    
				//WebSocketSession mEndSocket = (WebSocketSession) mLiveSession.get(messageContentDTO.getReceiveUserId());
				if (mEndSocket != null) {
					mEndSocket.sendMessage(new TextMessage(gson
							.toJson(messageDTOObj)));
				}

				messageDTOObj.setStatus("chat-res");
				// JSONObject jsonObjSender = new JSONObject(messageDTOObj);

				Gson gsonObj = new Gson();
				gsonObj.toJson(messageDTOObj);
				// find the sender socket and push message
				WebSocketSession senderSocket= (WebSocketSession) WebscoketBean.getmLiveSession().get(messageContentDTO.getSenderUserId());
	    		  
				//WebSocketSession senderSocket = (WebSocketSession) mLiveSession.get(messageContentDTO.getSenderUserId());
				if (senderSocket != null) {

					senderSocket.sendMessage(new TextMessage(gsonObj
							.toJson(messageDTOObj)));
				}

			}
	    	
				
		} else if (status.equalsIgnoreCase("groupMessage")) {

			Gson gson = new Gson();
			MessageContentDTO messageContentDTO = gson.fromJson(
					message.getPayload(), MessageContentDTO.class);

			GroupMessageDTO grpMessageDTO = DemoBeanUtil.getChatService()
					.sendGroupTextMessage(messageContentDTO);
			if (grpMessageDTO != null) {

				messageDTOObj.setGroupMessageDTO(grpMessageDTO);
				messageDTOObj.setStatus("groupchat");
				messageDTOObj.setSenderUk(grpMessageDTO.getGroupUkId());
				// JSONObject jsonObjSender = new JSONObject(messageDTOObj);

				List<GroupMembers> groupMembersList = DemoBeanUtil
						.getChatService()
						.getAllActiveMemberOfGrpByGroupIdForMessagePush(
								messageContentDTO.getReceiveUserId());
				if (groupMembersList != null) {


					Gson gsonObj = new Gson();
					gsonObj.toJson(messageDTOObj);
					// System.out.println(messageContentDTO.getSenderUserId());
					for (GroupMembers members : groupMembersList) {

						if (!messageContentDTO.getSenderUserId().equalsIgnoreCase(members.getUser().getId())) {
							 
							// find the online user receiver socket and push message
							
							WebSocketSession groupMemberScoket= (WebSocketSession) WebscoketBean.getmLiveSession().get(members.getUser().getId());
					    	
							//	WebSocketSession groupMemberScoket = (WebSocketSession) mLiveSession.get(members.getUser().getId());
								if(groupMemberScoket!=null){
									groupMemberScoket.sendMessage(new TextMessage(gsonObj
											.toJson(messageDTOObj)));
								}
								
							
						}

					}

				}

				// find the sender socket and push message
				
				WebSocketSession senderSocket= (WebSocketSession) WebscoketBean.getmLiveSession().get(messageContentDTO.getSenderUserId());
				 
				//WebSocketSession senderSocket = (WebSocketSession) mLiveSession.get(messageContentDTO.getSenderUserId());

				if (senderSocket != null) {
					
					messageDTOObj = new MessageScoketDTO();
					messageDTOObj.setGroupMessageDTO(grpMessageDTO);
					messageDTOObj.setStatus("groupchat-response");
					// JSONObject jsonObjSenderSender = new
					// JSONObject(messageDTOObj);

					Gson gsonObj = new Gson();

					gsonObj.toJson(messageDTOObj);

					senderSocket.sendMessage(new TextMessage(gsonObj
							.toJson(messageDTOObj)));

				}

			}
		}else if(status.equalsIgnoreCase("logout")){
	    		  String userId=jsonObject.get("senderUserId").toString();
	    		  
	    		  //update user table status isOnline flase
	    		  DemoBeanUtil.getChatService().updateUserOnlineStatus(userId, false);
	    		  
					List<ContactList> onlineMembersList = DemoBeanUtil
							.getChatService().findAllOnlineUsersByUserId(userId,true);
					
					if(onlineMembersList!=null){
						//push the  user is  goes to off line(logout)
						messageDTOObj= new MessageScoketDTO();
						messageDTOObj.setStatus("onlineStatus");
						messageDTOObj.setOnlineStatus(false);//off-line
						messageDTOObj.setSenderUserId(userId);
						Gson gsonObj = new Gson();
		    		    gsonObj.toJson(messageDTOObj);
		    		    
		    		    for(ContactList friendList:onlineMembersList){
		    		    	
		    		    	  // WebSocketSession senderSocket=(WebSocketSession)mLiveSession.get(friendList.getFriendId().getId());
		    		    	WebSocketSession senderSocket= (WebSocketSession) WebscoketBean.getmLiveSession().get(friendList.getFriendId().getId());
		    				
		    		    	   if(senderSocket!=null){
		   	    				 senderSocket.sendMessage(new TextMessage( gsonObj.toJson(messageDTOObj)));
		   	    			   }
		    		    	  
		    		    }
						
		    		   
					}
					WebscoketBean.getmLiveSession().remove(userId);
					
					System.out.println("User removed "+userId);
					// mLiveSession.remove(userId); // remove  user from the  Map
					
	    	}
	   	Iterator<Entry<String, Object>> iterator = WebscoketBean.getmLiveSession().entrySet().iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next().getKey());
		
		}
		 
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		// TODO Auto-generated method stub
		//super.handleBinaryMessage(session, message);
		
		 System.out.println("New Binary Message Received");
		 session.sendMessage(message);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionEstablished(session);
		System.out.println("webscoket connected...!!!");
		//updateUserList();
		 
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
		
		//mLiveSession.remove(session.getId());
		//mLiveUsers.remove(session.getId());
		
		System.out.println("User removed "+session.getId());
		
		//updateUserList();
	}
	
	public void updateUserList() throws IOException {
		Iterator<Entry<String, Object>> iterator = WebscoketBean.getmLiveSession().entrySet().iterator();
		
		
		while(iterator.hasNext()) {
			WebSocketSession session = (WebSocketSession) iterator.next().getValue();
			
			String mLiveUserList = objectMapper.writeValueAsString(WebscoketBean.getmLiveSession());
			session.sendMessage(new TextMessage("live-user="+mLiveUserList));	
				
		}
	}

}
