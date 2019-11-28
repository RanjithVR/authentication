package jp.profield.chat.service.impl;

import java.util.List;

import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.FriendRequest;
import jp.profield.chat.models.repository.ContactListRepository;
import jp.profield.chat.service.ContactListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ContactListServiceImpl implements ContactListService{

	@Autowired 
	private ContactListRepository contactListRepository;

	@Override
	public ContactList saveContactList(ContactList contactList) {
		
		return contactListRepository.save(contactList);
	}

	@Override
	public ContactList findContactListExsistByUserIdAndFrndId(String userId,
			String frndId) {
		return contactListRepository.findContactListExsistByUserIdAndFrndId(userId, frndId);
	}

	@Override
	public List<ContactList> findAllActiveContactByUserId(String userId,
			boolean status) {
		return contactListRepository.findAllActiveContactByUserId(userId, status);
	}

	@Override
	public List<ContactList> findAllContactNotIntheGroupbyUserId(String userId,
			String[] grpMemebrsId) {
		return contactListRepository.findAllContactNotIntheGroupbyUserId(userId, grpMemebrsId);
	}

	@Override
	public List<ContactList> findAllContactListByUserId(String userId) {
	
		return contactListRepository.findAllContactListByUserId(userId);
	}

	@Override
	public List<ContactList> findAllOnlineUserByUserId(String userId,boolean isOnline) {
	
		return contactListRepository.findAllOnlineUserByUserId(userId,isOnline);
	}

	@Override
	public ContactList findContactListExsistByUserIdAndFrndIdAndIsDeleted(
			String userId, String frndId, boolean isDeleted) {
		
		return contactListRepository.findContactListExsistByUserIdAndFrndIdAndIsDeleted(userId, frndId, isDeleted);
	}

	
}
