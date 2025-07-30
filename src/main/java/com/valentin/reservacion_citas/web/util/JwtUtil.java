package com.valentin.reservacion_citas.web.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

	private final String jwtIssuer;
	private final Algorithm algorithm;

	public JwtUtil(@Value("${jwt.issuer}") String jwtIssuer, @Value("${jwt.secret}") String jwtSecret) {
		this.jwtIssuer = jwtIssuer;
		this.algorithm = Algorithm.HMAC256(jwtSecret);
	}

	public String createJwt(String email) {
		Date dateNow = new Date();
		Date expiresAt = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

		return JWT.create()
				  .withSubject(email)
				  .withIssuer(jwtIssuer)
				  .withIssuedAt(dateNow)
				  .withExpiresAt(expiresAt)
				  .sign(algorithm);
	}

	public Boolean validJwt(String jwt) {
		try {
			JWT.require(algorithm)
			   .build()
			   .verify(jwt);

			return true;
		} catch (JWTVerificationException e) {
			return false;
		}
	}

	public String getUsername(String jwt) {
		return JWT.require(algorithm)
				  .build()
				  .verify(jwt)
				  .getSubject();
	}
}
