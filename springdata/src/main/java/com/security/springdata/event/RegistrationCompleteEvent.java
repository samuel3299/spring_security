package com.security.springdata.event;

import org.springframework.context.ApplicationEvent;

import com.security.springdata.entity.User;

import lombok.Getter;
import lombok.Setter;

/**
 * Creates an event Publisher
 */
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

	private User user;

	private String applicationUrl;

	/**
	 * saves the user and url
	 * @param user user data
	 * @param applicationUrl url
	 */
	public RegistrationCompleteEvent(User user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}

}
