package com.cat.jpa.dao;

import com.cat.jpa.entity.Unit;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.vo.UnitVO;

import java.util.Collection;
import java.util.List;

public interface UnitDao extends CommonDao<Unit, Long> {

	Unit find(Long buildId, String name);

	List<Unit> findList(Long buildId, String name, Page page, Sort sort);

	long count(Long buildId, String name);

	List<Unit> findList(Collection<Long> buildIds, String projectName, String buildName, String name, Page page, Sort sort);

	long count(Collection<Long> buildIds, String projectName, String buildName, String name);

	List<UnitVO> findVOList(Collection<Long> buildIds, String projectName, String buildName, String name, Page page, Sort sort);
}
