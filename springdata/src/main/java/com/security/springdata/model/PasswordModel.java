package com.security.springdata.model;

import lombok.Data;

/**
 * Model layer for password
 */
@Data
public class PasswordModel {

	String email;
	String oldPassword;
	String newPassword;
}
