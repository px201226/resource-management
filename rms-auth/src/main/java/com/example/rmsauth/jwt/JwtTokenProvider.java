package com.example.rmsauth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORITIES_KEY = "roles";
	private static final long ACCESS_TOKEN_VALID_SECOND = 1; // 1시간만 토큰 유효
	private static final long REFRESH_TOKEN_VALID_SECOND = 3600; // 1시간만 토큰 유효

	private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	@Value("${jwt.secret}")
	private String secretKey;

	// Jwt 토큰 생성
	public String createToken(String userPk, List<String> roles) {

		final LocalDateTime now = LocalDateTime.now();
		Date generateTime = Timestamp.valueOf(now);
		Date expirationTime = Timestamp.valueOf(LocalDateTime.now().plusSeconds(ACCESS_TOKEN_VALID_SECOND));

		return Jwts.builder()
				.setHeader(createHeader())
				.setClaims(createClaims(userPk, roles)) // 데이터
				.setIssuedAt(generateTime) // 토큰 발행일자
				.setExpiration(expirationTime) // set Expire Time
				.signWith(createSigningKey(secretKey))
				.compact();
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims =
					Jwts
					.parserBuilder()
					.setSigningKey(createSigningKey(secretKey))
					.build()
					.parseClaimsJws(jwtToken);

			return true;
//			return !claims.getBody().getExpiration().before(new Date());
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			log.debug("잘못된 토큰 형식 ");
			return false;
		} catch (ExpiredJwtException e){
			return false;
		}
	}

	private Claims createClaims(final String userPk, final List<String> roles) {
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put(AUTHORITIES_KEY, roles);

		return claims;
	}

	private Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();

		header.put(Header.TYPE, Header.JWT_TYPE);

		return header;
	}

	private Key createSigningKey(String key) {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
		return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
	}

	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token) {
//		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//
////        Claims claims = parseClaims(token);
////        Collection<? extends GrantedAuthority> authorities =
////                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
////                        .map((str)->str.replace("[",""))
////                        .map((str)->str.replace("]",""))
////                        .map(SimpleGrantedAuthority::new)
////                        .collect(Collectors.toList());


		return new UsernamePasswordAuthenticationToken(null , "", null);
	}

	// Jwt 토큰에서 회원 구별 정보 추출
	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}


	// Jwt 토큰에서 회원 구별 정보 추출
	private String getUserPk(String token) {
		return  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	// Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
	public String resolveToken(HttpServletRequest req) {
		return req.getHeader(AUTHORIZATION_HEADER);
	}


}