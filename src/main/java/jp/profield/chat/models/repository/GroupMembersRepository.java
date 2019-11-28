package jp.profield.chat.models.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import jp.profield.chat.model.GroupMembers;

public interface GroupMembersRepository extends MongoRepository<GroupMembers, String> {

	@Query("{ 'group.id': ?0}")
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupId(String groupId);
	@Query("{ 'group.id': ?0, 'user.id': ?1}")
	GroupMembers getCurrentUserGrpInfoByUserIdAndGrpId(String groupId,String userId);
	
	@Query("{ 'group.id': ?0 }")
	public List<GroupMembers> getAllGrpMembersIdByGroupId(String groupId,boolean isActive);
	
	
/*	@Query(value="{ $text: { $search: \'0?\' } ,vendorId : ?1 }" ,
			fields="{ 'title' : 1, 'description' : 1, 'textScore' : 1}")
	List<GroupMembers> findByTermAndVendorId( String title ,String vendorId);*/
	
	
	@Query("{ 'group.id': ?0}")
	public List<GroupMembers> getAllActiveMemberOfGrpByGroupIdForMessagePush(
			String groupId);
	
 /*	
   @query("{ 'group.id': ?0 } ",fields="{ '-id' : 1}")
  public List<GroupMembers> getAllGrpMembersIdByGroupId1(String groupId,boolean isActive)*/
	
	@Query("{ 'user.id': ?0, 'is_deleted': ?1}")
	public List<GroupMembers> findAllGroupsByUserId(String userId,boolean isdelete);
	
	@Query("{ 'group.id': ?0, 'group_role': ?1}")
	public List<GroupMembers> findAnyAdminExistByGroupId(String groupId,int role);

	
}
