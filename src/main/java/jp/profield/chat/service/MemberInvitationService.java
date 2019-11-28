package jp.profield.chat.service;

import java.util.List;
import java.util.Optional;

import jp.profield.chat.model.MemberInvitation;
import jp.profield.chat.model.UserManagement;

public interface MemberInvitationService {

	
	MemberInvitation saveMemberInvitation( MemberInvitation memberInvitation);
	
    Optional<MemberInvitation> findByEmailAndUserId(String email,String userId);
    
    
    List<MemberInvitation> findAllinvitationByNewUserEmailId(String email);
}
