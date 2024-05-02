package com.security.springdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.springdata.entity.User;

/**
 * Repository layer for User
 */
public interface RegistrationRepository extends JpaRepository<User, Integer> {

	/**
	 * getting User data with mail as input
	 * @param mail user mail
	 * @return retiurns user entity
	 */
	User findByEmail(String mail);

}
