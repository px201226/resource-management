package com.example.rmsauth.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Member {

	@Id @GeneratedValue
	private Long seq;

	private String email;

	private String password;

	private Authority authority;


}
