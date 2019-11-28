package jp.profield.chat.service;

import java.util.Optional;

import jp.profield.chat.model.UserManagement;
import jp.profield.chat.web.rest.dto.UserDTO;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service Interface for managing Security.
 * 
 * @version 1.0
 */
public interface SecurityService extends UserDetailsService {

	Optional<UserDTO> findByUserName(String userName);

	void save(UserManagement user);

}
