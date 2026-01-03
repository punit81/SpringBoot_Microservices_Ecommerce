package com.discoveryService;

//import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityConfigurer(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/eureka/peerreplication/batch/**").permitAll()
					.anyRequest().authenticated()
					)
			.httpBasic(Configurer -> Customizer.withDefaults());
		return http.build();
		
	}
	
	@Bean
	public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
	    UserDetails user = User.withUsername("user")
	        .password(passwordEncoder.encode("password"))
	        .roles("USER")
	        .build();

	    return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
