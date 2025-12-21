package com.apigateway.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/eureka/**").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(
					jwt->jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter())));
			
		return http.build();
	}

	private Converter<Jwt, ? extends AbstractAuthenticationToken> customJwtAuthenticationConverter() {
		// TODO Auto-generated method stub
		return jwt->{
			 Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");

		        Collection<GrantedAuthority> authorities = List.of();
		        if (realmAccess != null && realmAccess.containsKey("roles")) {
		            List<String> roles = (List<String>) realmAccess.get("roles");

		            authorities = roles.stream()
		                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
		                    .collect(Collectors.toList());
		        }

		        // Return a JwtAuthenticationToken with the authorities
		        return new JwtAuthenticationToken(jwt, authorities);
		    };
	}
}
