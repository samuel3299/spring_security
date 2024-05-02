package com.security.springdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.springdata.entity.VerificationToken;

/**
 * Repository layer for VerificationToken
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

	
	/**
	 * getting token details with token as input
	 * @param token verification token
	 * @return returns VerificationToken entity
	 */
	VerificationToken findByToken(String token);

}
