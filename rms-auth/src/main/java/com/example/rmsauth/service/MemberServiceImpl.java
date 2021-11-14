package com.example.rmsauth.service;



import com.example.rmsauth.entity.Member;
import com.example.rmsauth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements IMemberService {

	private final MemberRepository memberRepository;


	@Override
	public Member findByEmail(final String email) {

		return null;
	}
}
