package com.example.rmsauth.service;


import com.example.rmsauth.entity.Member;

public interface IMemberService {

	Member findByEmail(String email);
}
