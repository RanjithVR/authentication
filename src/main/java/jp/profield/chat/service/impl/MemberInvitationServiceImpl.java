package jp.profield.chat.service.impl;

import java.util.List;
import java.util.Optional;

import jp.profield.chat.model.MemberInvitation;
import jp.profield.chat.models.repository.MemberInvitationRepository;
import jp.profield.chat.service.MemberInvitationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class MemberInvitationServiceImpl implements MemberInvitationService {

	
	@Autowired
	private MemberInvitationRepository invitationRepository;

	@Override
	public MemberInvitation saveMemberInvitation(MemberInvitation memberInvitation) {
	
		return invitationRepository.save(memberInvitation);
	}

	@Override
	public Optional<MemberInvitation> findByEmailAndUserId(String email,
			String userId) {
		
		return invitationRepository.findByEmailIdAndUserId(email, userId);
	}

	@Override
	public List<MemberInvitation> findAllinvitationByNewUserEmailId(
			String email) {
		return invitationRepository.findAllinvitationByNewUserEmailId(email);
	}
	
	
	
	
}
