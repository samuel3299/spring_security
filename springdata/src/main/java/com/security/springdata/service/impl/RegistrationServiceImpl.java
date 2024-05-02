package com.security.springdata.service.impl;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.springdata.entity.PasswordResetToken;
import com.security.springdata.entity.User;
import com.security.springdata.entity.VerificationToken;
import com.security.springdata.model.UserModel;
import com.security.springdata.repository.PasswordResetTokenRepository;
import com.security.springdata.repository.RegistrationRepository;
import com.security.springdata.repository.VerificationTokenRepository;
import com.security.springdata.service.RegistrationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implemetation layer to implement @RegistrationService Interface
 */
@Service
@Slf4j
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

	private RegistrationRepository registrationRepository;

	private VerificationTokenRepository verificationTokenRepository;

	private PasswordEncoder passwordEncoder;

	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public User registerUser(UserModel userModel) {
		// TODO Auto-generated method stub
		log.info("hi");
		User user = new User();
		user.setEmail(userModel.getEmail());
		user.setFirstName(userModel.getFirstName());
		user.setLastName(userModel.getLastName());
		user.setRole("User");
		user.setPassword(passwordEncoder.encode(userModel.getPassword()));
		registrationRepository.save(user);
		return user;
	}

	@Override
	public void saveVerificationToken(User user, String token) {
		VerificationToken verificationToken = new VerificationToken(user, token);
		verificationTokenRepository.save(verificationToken);
	}

	@Override
	public String verifyregistration(String token) {
		// TODO Auto-generated method stub
		VerificationToken verificationToken = new VerificationToken(null, null);
		verificationToken = verificationTokenRepository.findByToken(token);
		if (verificationToken.getUser() != null) {
			log.info(verificationToken + " ");
		}
		if (verificationToken.getUser() == null) {
			return "invalid";
		}
		User user = verificationToken.getUser();
		Calendar calendar = Calendar.getInstance();
		if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
			verificationTokenRepository.delete(verificationToken);
			return "token Expired";
		}
		user.setEnabled(true);
		registrationRepository.save(user);
		return "valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldtoken) {
		VerificationToken verificationToken = new VerificationToken(null, null);
		verificationToken = verificationTokenRepository.findByToken(oldtoken);
		verificationToken.setToken(UUID.randomUUID().toString());
		verificationTokenRepository.save(verificationToken);
		return verificationToken;
	}

	@Override
	public User findUsermail(String email) {
		return registrationRepository.findByEmail(email);
	}

	@Override
	public void sendResetMail(User user, String token) {
		 PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
		passwordResetTokenRepository.save(passwordResetToken);
		passwordResetToken = passwordResetTokenRepository.findByToken(token);
		passwordResetToken = passwordResetTokenRepository.findByToken(token);
		log.info(passwordResetToken + " ");
	}

	@Override
	public String validatePasswordResetToken(String token) {
//		PasswordResetToken passwordResetToken = new PasswordResetToken(null,null);
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
		log.info(passwordResetToken + " ");
		if (passwordResetToken.getUser() != null) {
			log.info(passwordResetToken + " ");
		}
		if (passwordResetToken == null) {
			return "invalid";
		}
		log.info("hi");
		User user = passwordResetToken.getUser();
		log.info(user + " ");
		Calendar calendar = Calendar.getInstance();
		if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
			passwordResetTokenRepository.delete(passwordResetToken);
			return "password reset time Expired";
		}
		return "valid";
	}

	@Override
	public Optional<User> getUserByPasswordResetToken(String token) {
		// TODO Auto-generated method stub
		User user = new User();
		user = passwordResetTokenRepository.findByToken(token).getUser();
		log.info(user + " ");
		return Optional.ofNullable(user);
	}

	@Override
	public void changeUserPassword(User user, String newPassoword) {
		user.setPassword(passwordEncoder.encode(newPassoword));
		registrationRepository.save(user);

	}

	@Override
	public boolean checkForValidOldPassword(User user, String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

}
