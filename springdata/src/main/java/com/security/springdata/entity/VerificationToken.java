package com.security.springdata.entity;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Verification Tokens Entity
 */
@Entity
@Table(name = "verify_tokens")
@Data
@NoArgsConstructor
public class VerificationToken {

	private static final int EXPIRATION_TIME = 10;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String token;

	private Date ExpirationTime;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	/**
	 * Creates a verification token with user and token parameters
	 * @param user user details
	 * @param token token to verify
	 */
	public VerificationToken(User user, String token) {
		super();
		this.user = user;
		this.token = token;
		this.ExpirationTime = calculateExpirationDate(EXPIRATION_TIME);
	}

	/**
	 * Creates a verification token with token as parameter
	 * @param token token to verify
	 */
	public VerificationToken(String token) {
		super();
		this.token = token;
		this.ExpirationTime = calculateExpirationDate(EXPIRATION_TIME);
	}

	/**
	 * creates an instance of Expiration time for password
	 * @param expirationTime2 expire time
	 * @return returns Date at which token expires
	 */
	private Date calculateExpirationDate(int expirationTime2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, expirationTime2);
		return new Date(calendar.getTime().getTime());
	}
}
