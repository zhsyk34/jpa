package com.cat.jpa.dao;

import com.cat.jpa.entity.Project;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;

import java.time.LocalDate;
import java.util.List;

public interface ProjectDao extends CommonDao<Project, Long> {

	Project find(String name);

	List<Project> findList(String name, LocalDate begin, LocalDate end, Page page, Sort sort);

	long count(String name, LocalDate begin, LocalDate end);

}
