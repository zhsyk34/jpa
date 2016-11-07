package com.cat.jpa.dao;

import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Rule;
import com.cat.jpa.tool.jpa.Sort;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HouseDaoTest extends InitTest {
	@Test
	public void find() throws Exception {
		houseDao.find(3L, "hn");
	}

	@Test
	public void findList() throws Exception {
		Page page = Page.of(1, 5);
		Sort sort = Sort.of("id", Rule.DESC);
		houseDao.findList(2L, 7L, "house", page, sort);
	}

	@Test
	public void count() throws Exception {
		houseDao.count(2L, 7L, "house");
	}

	@Test
	public void findList1() throws Exception {
		Page page = Page.of(1, 5);
		Sort sort = Sort.of("id", Rule.DESC);
		List<Long> bids = Arrays.asList(12L, 33L, 44L);
		houseDao.findList(bids, "pn", "bn", "un", "hn", page, sort);
	}

	@Test
	public void count1() throws Exception {
		List<Long> bids = Arrays.asList(12L, 33L, 44L);
		houseDao.count(bids, "pn", "bn", "un", "hn");
	}

	@Test
	public void findVOList() throws Exception {
		Page page = Page.of(1, 5);
		Sort sort = Sort.of("id", Rule.DESC);
		List<Long> bids = Arrays.asList(12L, 33L, 44L);
		houseDao.findVOList(bids, "pn", "bn", "un", "hn", page, sort);
	}

}