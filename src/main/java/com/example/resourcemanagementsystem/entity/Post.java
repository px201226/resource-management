package com.example.resourcemanagementsystem.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Post {

	@Id
	private Long id;
}
