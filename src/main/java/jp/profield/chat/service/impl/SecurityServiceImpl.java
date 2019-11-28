package jp.profield.chat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.service.SecurityService;
import jp.profield.chat.web.rest.dto.UserDTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityServiceImpl implements SecurityService {

	private Log log = LogFactory.getLog(this.getClass());

	@Inject
	private UserRepository userRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		log.info("==>calling loadUserByUsername for the user " + userName);
		Optional<UserManagement> userAccount = userRepository.findByUserNameIgnoreCase(userName);
		if (!userAccount.isPresent()) {
			throw new BadCredentialsException("UserName " + userName + " not found");
		}
		if (!userAccount.get().isActivated()) {
			throw new BadCredentialsException("UserName " + userName + " is not active");
		}

		UserManagement user = userAccount.get();
		return new User(user.getUserName(), user.getPassword(), true, true, true, true, getGrantedAuthorities(user));
	}

	@Override
	@Transactional
	public void save(UserManagement userAccount) {
		userRepository.save(userAccount);
	}

	private List<GrantedAuthority> getGrantedAuthorities(UserManagement userAccount) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_NONE"));
		authorities.add(new SimpleGrantedAuthority(userAccount.getRole().getRoleName()));
		return authorities;
	}

	@Override
	public Optional<UserDTO> findByUserName(String userName) {
		log.info("==>calling loadUserByUsername for the user " + userName);
		return userRepository.findByUserNameIgnoreCase(userName).map(usr -> {
			UserDTO userDTO = new UserDTO(usr);
			return userDTO;
		});
	}
}
