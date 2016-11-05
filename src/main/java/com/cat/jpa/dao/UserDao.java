package com.cat.jpa.dao;

import com.cat.jpa.entity.User;
import com.cat.jpa.entity.dict.Gender;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;

import java.time.LocalDate;
import java.util.List;

public interface UserDao extends CommonDao<User, Long> {

    User find(String name, String password);

    List<User> findList(String name, Gender gender, LocalDate begin, LocalDate end, Page page, Sort sort);

    long count(String name, Gender gender, LocalDate begin, LocalDate end);

}
