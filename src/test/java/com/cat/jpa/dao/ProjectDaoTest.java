package com.cat.jpa.dao;

import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Rule;
import com.cat.jpa.tool.jpa.Sort;
import org.junit.Test;

import java.time.LocalDate;

public class ProjectDaoTest extends InitTest {
    @Test
    public void find() throws Exception {
        projectDao.find("a");
    }

    @Test
    public void findList() throws Exception {
        Page page = Page.of(1, 5);
        Sort sort = Sort.of("id", Rule.DESC);
        projectDao.findList("a", LocalDate.of(2011, 12, 13), LocalDate.of(2016, 8, 9), page, sort);
    }

    @Test
    public void count() throws Exception {
        projectDao.count("a", LocalDate.of(2011, 12, 13), LocalDate.of(2016, 8, 9));
    }

}