package com.example.rmsauth.jwt;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
class JwtTokenProviderTest {

	@Test
	void 토큰생성_테스트() {

		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
		ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "1213213122332132132134678221321316738912738127398121");

		final String token = jwtTokenProvider.createToken("px2007@naver.com", Lists.emptyList());

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(jwtTokenProvider.validateToken(token));
		log.debug("token : ({}) ", token);
	}
}