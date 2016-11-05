package com.cat.jpa.dao;

import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Rule;
import com.cat.jpa.tool.jpa.Sort;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BuildDaoTest extends InitTest {
    @Test
    public void find() throws Exception {
        buildDao.find(1L, "a");
    }

    @Test
    public void findList() throws Exception {
        Page page = Page.of(1, 5);
        Sort sort = Sort.of("id", Rule.DESC);
        buildDao.findList(2L, "a", null, null, page, sort);
    }

    @Test
    public void count() throws Exception {
        buildDao.count(2L, "a", null, null);
    }

    @Test
    public void findList1() throws Exception {
        Page page = Page.of(1, 5);
        Sort sort = Sort.of("id", Rule.DESC);
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        buildDao.findList(ids, "a", null, null, page, sort);
    }

    @Test
    public void count1() throws Exception {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        buildDao.count(ids, "a", null, null);
    }

    @Test
    public void findVOList() throws Exception {
        Page page = Page.of(1, 5);
        Sort sort = Sort.of("id", Rule.DESC);
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        buildDao.findVOList(ids, "a", null, null, page, sort);
    }

}