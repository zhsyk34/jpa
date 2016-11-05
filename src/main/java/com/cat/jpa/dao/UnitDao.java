package com.cat.jpa.dao;

import com.cat.jpa.entity.Unit;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;

import javax.persistence.Tuple;
import java.util.Collection;
import java.util.List;

public interface UnitDao extends CommonDao<Unit, Long> {

    List<Unit> findList(Long buildId, String name);

    List<Unit> findList(Collection<Long> buildIds, String buildName, Collection<Long> unitIds, String unitName, Page page, Sort sort);

    long count(Collection<Long> buildIds, String buildName, Collection<Long> unitIds, String unitName);

    List<Tuple> findTuple(Collection<Long> buildIds, String buildName, Collection<Long> unitIds, String unitName, Page page, Sort sort);

//    List<UnitVO> findVOList(Collection<Long> buildIds, String buildName, Collection<Long> unitIds, String unitName, Page page, Sort sort);
//
//    List<UnitVO> findVOList2(Collection<Long> buildIds, String buildName, Collection<Long> unitIds, String unitName, Page page, Sort sort);

}
