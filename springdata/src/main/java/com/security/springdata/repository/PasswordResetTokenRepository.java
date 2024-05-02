package com.security.springdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.springdata.entity.PasswordResetToken;

/**
 * Repository layer for PasswordResetToken
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

	/**
	 * getting passwordresetToken entity with token as parameter
	 * @param token reset token
	 * @return returns an Entity
	 */
	PasswordResetToken findByToken(String token);

}
