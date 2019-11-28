package jp.profield.chat.models.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import jp.profield.chat.model.UserManagement;

/**
 * A Repository for the UserManagement Entity 
 * @author Ranjith
 * @since 27-Aug-2018
 * @version 1.0
 */
public interface UserRepository extends MongoRepository<UserManagement, String> {

	Optional<UserManagement> findByUserNameIgnoreCase(String userName);

	Optional<UserManagement> findByUkMessageKey(Long uniquKey);

	Optional<UserManagement> findOneByid(String id);
	
	Optional<UserManagement> findByEmail(String emailId);
	
	 @Query(value = "  { fullName: { $regex:?0 } }")
	List<UserManagement> findUserByName(String name);
	 
		@Query("{ 'email': ?0, 'isDeleted':?1}")
    Optional<UserManagement> findByEmailAndActivated(String email,boolean activated);
	 
	@Query("{ 'email': ?0, 'userName': ?1,'isDeleted':?2}")
	Optional<UserManagement> findUserByEmailAndUserNameAndActivated(String emailId,String userName,boolean isActive);

	
    @Query(value = "{ 'email': ?0 }")
	List<UserManagement> findUserByEmail(String email);
    
    @Query(value = "{ 'userName': ?0,'is_deleted':?1 }")
    UserManagement findUserByUserNameAndActive(String userName,boolean is_deleted);
    
    @Query(value = "{ 'userName': ?0,'is_deleted':?1 }")
    Optional<UserManagement> findByUserNameIgnoreCaseAndDeleteStatusForLogin(String userName);
	

}
