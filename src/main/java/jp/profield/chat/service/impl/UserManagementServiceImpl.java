package jp.profield.chat.service.impl;

import java.util.Optional;

import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.service.UserManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public Optional<UserManagement> findByUserNameIgnoreCase(String userName) {

		return userRepository.findByUserNameIgnoreCase(userName);
	}

	@Override
	public UserManagement saveUser(UserManagement userManagement) {
		return userRepository.save(userManagement);
	}

	@Override
	public Optional<UserManagement> findByEmail(String mail) {
	
		return userRepository.findByEmail(mail);
	}

	@Override
	public Optional<UserManagement> findOneByid(String userId) {
		
		return userRepository.findOneByid(userId);
	}

	@Override
	public Optional<UserManagement> findByEmailAndActivated(String email,
			boolean activated) {
		
		return userRepository.findByEmailAndActivated(email, activated);
	}

}
