package com.example.rmscore.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;

public class JwtTokenResolver {

	@Value("${jwt.secret}")
	private String secretKey;

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims =
					Jwts
							.parserBuilder()
							.setSigningKey(createSigningKey(secretKey))
							.build()
							.parseClaimsJws(jwtToken);

			return !claims.getBody().getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	private Key createSigningKey(String key) {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
		return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
	}

}
