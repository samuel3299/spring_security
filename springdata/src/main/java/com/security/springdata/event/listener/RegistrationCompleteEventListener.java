package com.security.springdata.event.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.security.springdata.entity.User;
import com.security.springdata.event.RegistrationCompleteEvent;
import com.security.springdata.service.RegistrationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Creates an Event Listener
 */
@Component
@AllArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	private RegistrationService registrationService;

	/**
	 * overridden function for application event
	 */
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		// Creating Verifcation Token
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		registrationService.saveVerificationToken(user, token);
		// creating verify url to mail
		String url = event.getApplicationUrl() + "/security/verifyRegistration?token=" + token;
		log.info("Click the link to very your url: {}" + url);
	}

}
