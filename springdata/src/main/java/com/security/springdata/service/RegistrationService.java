package com.security.springdata.service;

import java.util.Optional;

import com.security.springdata.entity.User;
import com.security.springdata.entity.VerificationToken;
import com.security.springdata.model.UserModel;

/**
 * Service interface for registering the user
 */
public interface RegistrationService {

	/**
	 * Registering the user
	 * @param userModel model details
	 * @return returns a user entity
	 */
	User registerUser(UserModel userModel);

	/**
	 * saves verification token
	 * @param user user data
	 * @param token token number
	 */ 
	void saveVerificationToken(User user, String token);

	/**
	 * verifying the register
	 * @param token token number
	 * @return returns String if success or not
	 */
	String verifyregistration(String token);

	/**
	 * generates new Verification Token
	 * @param oldtoken old token
	 * @return returns an Entity layer
	 */
	VerificationToken generateNewVerificationToken(String oldtoken);

	/**
	 * finds user by mail
	 * @param email email
	 * @return returns user
	 */
	User findUsermail(String email);

	/**
	 *  send a mail to user
	 * @param user user details
	 * @param token token 
	 */
	void sendResetMail(User user, String token);

	/**
	 *  sends reset mail to user
	 * @param token token 
	 * @return returns a string if success
	 */
	String validatePasswordResetToken(String token);

	/**
	 * gets user by password reset token
	 * @param token token
	 * @return returns optional of user
	 */
	Optional<User> getUserByPasswordResetToken(String token);

	/**
	 * changes user password
	 * @param user user data
	 * @param newPassoword new password
	 */
	void changeUserPassword(User user, String newPassoword);

	/**
	 * checks if password is valid with old password
	 * @param user user data
	 * @param oldPassword old password
	 * @return return true or false
	 */
	boolean checkForValidOldPassword(User user, String oldPassword);

}
