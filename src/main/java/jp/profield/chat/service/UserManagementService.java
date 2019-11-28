package jp.profield.chat.service;

import java.util.Optional;

import jp.profield.chat.model.UserManagement;

public interface UserManagementService {
	Optional<UserManagement> findByUserNameIgnoreCase(String userName);
	
	UserManagement saveUser( UserManagement userManagement);
	
	Optional<UserManagement> findByEmail(String mail);
	
	Optional<UserManagement> findOneByid(String userId);
	
	 Optional<UserManagement> findByEmailAndActivated(String email,boolean activated);
}
