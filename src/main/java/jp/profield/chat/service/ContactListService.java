package jp.profield.chat.service;

import java.util.List;

import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.FriendRequest;

public interface ContactListService {

	
	 ContactList saveContactList(ContactList contactList);
	 ContactList findContactListExsistByUserIdAndFrndId(String userId,String frndId);
	 public List<ContactList> findAllActiveContactByUserId(String userId,boolean status);
	 
	 public List<ContactList> findAllContactNotIntheGroupbyUserId(String userId,String [] grpMemebrsId);
	 public List<ContactList> findAllContactListByUserId(String userId);
	 
	 public List<ContactList> findAllOnlineUserByUserId(String userId,boolean isOnline);
	 
	 ContactList findContactListExsistByUserIdAndFrndIdAndIsDeleted(String userId,String frndId,boolean isDeleted);
	 
	
	 
}
