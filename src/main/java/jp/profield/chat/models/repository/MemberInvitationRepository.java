package jp.profield.chat.models.repository;

import java.util.List;
import java.util.Optional;

import jp.profield.chat.model.MemberInvitation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MemberInvitationRepository extends MongoRepository<MemberInvitation, String> {

	@Query("{ 'invitee_email': ?0, 'user.id': ?1}")
	Optional<MemberInvitation> findByEmailIdAndUserId(String email,String userId);
	
	@Query("{ 'invitee_email': ?0}")
	List<MemberInvitation>findAllinvitationByNewUserEmailId(String emailId);
}
