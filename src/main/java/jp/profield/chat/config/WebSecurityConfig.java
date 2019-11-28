
package jp.profield.chat.config;

import jp.profield.chat.security.jwt.JwtAuthEntryPoint;
import jp.profield.chat.security.jwt.JwtAuthTokenFilter;
import jp.profield.chat.security.uti.UserDetailsServiceImpl;
import jp.profield.chat.service.SecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * the web security configuration.
 * 
 * @since 16-May-2018
 * @version 1.0
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsServiceImpl userDetailsService;

		@Autowired
		private JwtAuthEntryPoint unauthorizedHandler;

		@Bean
		public JwtAuthTokenFilter authenticationJwtTokenFilter() {
			return new JwtAuthTokenFilter();
		}

		@Override
		public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
			authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.antMatcher("/api/**")
					.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
					.exceptionHandling().and().csrf().disable().headers().frameOptions().disable().and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authorizeRequests().antMatchers("/api/auth/signup").permitAll().antMatchers("/api/auth/savePassword").permitAll().antMatchers("/api/auth/forgotPassword").permitAll().antMatchers("/api/auth/signin")
					.permitAll().anyRequest().authenticated().and().exceptionHandling()
					.authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		}

	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		UserDetailsServiceImpl userDetailsService;

		@Autowired
		private CustomAuthenticationHandler customAuthenticationHandler;

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable().headers()
					.addHeaderWriter(
							new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
					.and().authorizeRequests().antMatchers("/", "/register/**", "/resources/**").permitAll()
					.antMatchers("/company/**").fullyAuthenticated().and().formLogin().loginPage("/login")
					.successHandler(customAuthenticationHandler).failureUrl("/login?error").permitAll().and().logout()
					.logoutUrl("/logout").deleteCookies("remember-me").logoutSuccessUrl("/login?logout").permitAll()
					.and().rememberMe();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			return encoder;
		}
	}
}