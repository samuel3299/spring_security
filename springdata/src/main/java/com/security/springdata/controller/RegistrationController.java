package com.security.springdata.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.springdata.entity.PasswordResetToken;
import com.security.springdata.entity.User;
import com.security.springdata.entity.VerificationToken;
import com.security.springdata.event.RegistrationCompleteEvent;
import com.security.springdata.model.PasswordModel;
import com.security.springdata.model.UserModel;
import com.security.springdata.repository.PasswordResetTokenRepository;
import com.security.springdata.service.RegistrationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/** 
 * Security Controller layer
 */
@RestController
@Slf4j
@RequestMapping("/security")
@AllArgsConstructor
public class RegistrationController {

	private RegistrationService registrationService;

	private ApplicationEventPublisher publisher;
	private PasswordResetTokenRepository passwordResetTokenRepository;

	/**
	 * Registering new User
	 * @param userModel contains User details
	 * @param request Publishing an event
	 * @return returns a response
	 */
	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
		User user = registrationService.registerUser(userModel);
		log.info(user.toString());
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return new ResponseEntity<>("success!", HttpStatus.OK);
	}

	/**
	 * validation of user token
	 * @param token verification token
	 * @return returns a response
	 */
	@GetMapping("/verifyRegistration")
	public ResponseEntity<Object> verifyregistration(@RequestParam("token") String token) {
		String result = registrationService.verifyregistration(token);
		if (result.equalsIgnoreCase("valid")) {
			return new ResponseEntity<>("User Verified!", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(result, HttpStatus.BAD_GATEWAY);
	}

	/**
	 * resends verification token
	 * @param oldtoken previous token
	 * @param request url request
	 * @return returns a response
	 */
	@GetMapping("/resendVerifyToken")
	public ResponseEntity<Object> resendVerificationToken(@RequestParam("token") String oldtoken,
			HttpServletRequest request) {
		VerificationToken verificationToken = registrationService.generateNewVerificationToken(oldtoken);
		User user = verificationToken.getUser();
		resendVerificationtomail(user, applicationUrl(request), verificationToken);
		return new ResponseEntity<>("Verification link sent!", HttpStatus.OK);
	}

	/**
	 * Resets password
	 * @param passwordModel Password details
	 * @param request url request
	 * @return returns a resonse
	 */
	@PostMapping("/resetPassword")
	public ResponseEntity<Object> resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
		User user = registrationService.findUsermail(passwordModel.getEmail());
		String url = "";
		if (user != null) {
			String token = UUID.randomUUID().toString();
			registrationService.sendResetMail(user, token);
			PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
			log.info(passwordResetToken + " ");
			log.info(user+" ");
			url = passwordResetTokenMail(user, applicationUrl(request), token);
			return new ResponseEntity<>(url, HttpStatus.OK);
		}
		return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
	}

	/**
	 * Saves a new Password to user
	 * @param token user token
	 * @param passwordModel password model 
	 * @return returns a response
	 */
	@PostMapping("/savePassword")
	public ResponseEntity<Object> savePassword(@RequestParam("token") String token,
			@RequestBody PasswordModel passwordModel) {
		String result = registrationService.validatePasswordResetToken(token);
		if (!result.equalsIgnoreCase("valid")) {
			return new ResponseEntity<>("reset link for password reset is expired", HttpStatus.REQUEST_TIMEOUT);
		}
		Optional<User> user = registrationService.getUserByPasswordResetToken(token);
		if (user.isPresent()) {
			registrationService.changeUserPassword(user.get(), passwordModel.getNewPassword());
			return new ResponseEntity<>("Pasword Reset Successsful!", HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid Password", HttpStatus.BAD_GATEWAY);
	}
	/**
	 * changes password to new password
	 * @param passwordModel contains password details
	 * @return returms a response
	 */
	@PostMapping("/changePassword")
	public  ResponseEntity<Object> changePassword(@RequestBody PasswordModel passwordModel){
		User user = registrationService.findUsermail(passwordModel.getEmail());
		if(!registrationService.checkForValidOldPassword(user, passwordModel.getOldPassword())) {
			return new ResponseEntity<>("Pasword didn't match with your old Password", HttpStatus.BAD_REQUEST);
		}
		registrationService.changeUserPassword(user, passwordModel.getNewPassword());
		return new ResponseEntity<>("Password changed Succesful!", HttpStatus.OK);
	}

	/**
	 * method to reset token mail for password
	 * @param user user details
	 * @param applicationUrl url 
	 * @param token password token
	 * @return returns url
	 */
	private String passwordResetTokenMail(User user, String applicationUrl, String token) {
		String url = applicationUrl + "/security/savePassword?token=" + token;
		log.info("Click the link to reset your password: {}" + url);
		return url;
	}

	/**
	 * resends verification mail
	 * @param user user details
	 * @param applicationUrl url
	 * @param verificationToken details of token to verify
	 */
	private void resendVerificationtomail(User user, String applicationUrl, VerificationToken verificationToken) {
		String url = applicationUrl + "/security/verifyRegistration?token=" + verificationToken.getToken();
		log.info("Click the link to very your url: {}" + url);

	}

	/**
	 * url request
	 * @param request url
	 * @return returns a string which is url
	 */
	private String applicationUrl(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
