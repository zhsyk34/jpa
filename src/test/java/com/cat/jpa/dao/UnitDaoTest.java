package com.cat.jpa.dao;

import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Rule;
import com.cat.jpa.tool.jpa.Sort;
import org.junit.Test;

public class UnitDaoTest extends InitTest {
    @Test
    public void findList() throws Exception {
        unitDao.find(2L, "un");
    }

    @Test
    public void findList1() throws Exception {
        Page page = Page.of(1, 5);
        Sort sort = Sort.of("id", Rule.DESC);
        unitDao.findList(2L, "xy", page, sort);
    }

    @Test
    public void count() throws Exception {

    }

    @Test
    public void findList2() throws Exception {

    }

    @Test
    public void count1() throws Exception {

    }

    @Test
    public void findVOList() throws Exception {

    }

}