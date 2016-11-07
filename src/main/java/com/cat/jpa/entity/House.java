package com.cat.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 60)
	private String name;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name = "unitId", foreignKey = @ForeignKey(name = "fk_house_unit"), nullable = false)
	@JsonIgnore
	private Unit unit;
}
