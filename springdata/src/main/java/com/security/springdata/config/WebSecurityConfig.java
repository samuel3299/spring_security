package com.security.springdata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuring web security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    PasswordEncoder pasworddecrypt() {
		return new BCryptPasswordEncoder(11);
	}

    /**
     * Securing the endpoint requests
     * @param http requests
     * @return returns a group of security filter chains containing web servlets
     * @throws Exception Exception occurs
     */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//disabling cors and csrf
		http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
		// Authorizing the requests
				.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
		return http.build();
	}
}
