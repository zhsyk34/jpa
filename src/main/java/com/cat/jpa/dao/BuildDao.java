package com.cat.jpa.dao;

import com.cat.jpa.entity.Build;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.vo.BuildVO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BuildDao extends CommonDao<Build, Long> {

    Build find(Long projectId, String name);

    List<Build> findList(Long projectId, String name, LocalDate begin, LocalDate end, Page page, Sort sort);

    long count(Long projectId, String name, LocalDate begin, LocalDate end);

    List<Build> findList(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort);

    long count(Collection<Long> ids, String name, LocalDate begin, LocalDate end);

    List<BuildVO> findVOList(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort);

}
