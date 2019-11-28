package jp.profield.chat.config;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * custom authentication using role.
 * @author Ranjith
 * @version 1.0
 */
@Component
public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		String dashboardTargetUrl = "/company/dashboard";
		String messages = "/company/chatHome";

		Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

		if (authorities.contains("USER")) {
			getRedirectStrategy().sendRedirect(request, response, messages);
		} else {
			getRedirectStrategy().sendRedirect(request, response, dashboardTargetUrl);
		}

	
	}

}