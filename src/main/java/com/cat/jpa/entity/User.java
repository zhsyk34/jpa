package com.cat.jpa.entity;

import com.cat.jpa.entity.dict.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 60)
	private String name;

	@Column(nullable = false)
	private String password;

	@Convert(converter = Gender.GenderConverter.class)
	private Gender gender = Gender.MALE;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<UserBuild> userBuilds;

}
