package com.cat.jpa.dao;

import com.cat.jpa.entity.House;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.vo.HouseVO;

import java.util.Collection;
import java.util.List;

public interface HouseDao extends CommonDao<House, Long> {

	House find(Long unitId, String name);

	List<House> findList(Long buildId, Long unitId, String name, Page page, Sort sort);

	long count(Long buildId, Long unitId, String name);

	List<House> findList(Collection<Long> buildIds, String projectName, String buildName, String unitName, String name, Page page, Sort sort);

	long count(Collection<Long> buildIds, String projectName, String buildName, String unitName, String name);

	List<HouseVO> findVOList(Collection<Long> buildIds, String projectName, String buildName, String unitName, String name, Page page, Sort sort);

}
