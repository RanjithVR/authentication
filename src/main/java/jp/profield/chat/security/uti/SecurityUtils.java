package jp.profield.chat.security.uti;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 * 
 * @version 1.0
 */
public final class SecurityUtils {

	@Inject
	private UserRepository userRepository;

	private SecurityUtils() {
	}

	/**
	 * Get the login of the current user.
	 *
	 * @return the login of the current user
	 */
	public static String getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String userName = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				userName = springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal();
			}
		}
		return userName;
	}

	/**
	 * If the current user has a specific authority (security role).
	 *
	 * <p>
	 * The name of this method comes from the isUserInRole() method in the Servlet
	 * API
	 * </p>
	 *
	 * @param authority
	 *            the authority to check
	 * @return true if the current user has the authority, false otherwise
	 */
	public static boolean isCurrentUserRole(String role) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			return authentication.getAuthorities().stream()
					.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
		}
		return false;
	}

	public  Optional<UserManagement> getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		Optional<UserManagement> user = userRepository.findByUserNameIgnoreCase(userName);
		return user;
	}

	public static boolean hasRole(String constants) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		if (roles.contains(constants)) {
			return true;
		}
		return false;
	}

}
