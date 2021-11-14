package com.example.rmscore.auth;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
class JwtTokenResolverTest {


	@Test
	void 토근_유효성_검사(){

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJweDIwMDdAbmF2ZXIuY29tIiwicm9sZXMiOltdLCJpYXQiOjE2MzY3OTY0OTQsImV4cCI6MTYzNjgwMDA5NH0.BpYGzjllypLeFoN1TeE8QkaUt1Rq7-ogdUb5DXTNaQ0";
		JwtTokenResolver jwtTokenResolver = new JwtTokenResolver();
		ReflectionTestUtils.setField(jwtTokenResolver, "secretKey", "1213213122332132132134678221321316738912738127398121");

		final boolean b = jwtTokenResolver.validateToken(token);
		System.out.println(b);
	}
}