package com.cat.jpa.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseVO {

	private Long id;

	private String name;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private Long unitId;

	private String unitName;

	private Long buildId;

	private String buildName;

	private Long projectId;

	private String projectName;

}
