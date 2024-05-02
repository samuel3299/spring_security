package com.security.springdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model layer for user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String confirmPassword;
}
