package com.cat.jpa.dao;

import com.cat.jpa.entity.User;
import com.cat.jpa.entity.dict.Gender;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Rule;
import com.cat.jpa.tool.jpa.Sort;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UserDaoTest extends InitTest {

    @Test
    public void save() throws Exception {
        User user = new User();
        user.setName("gm");
        user.setPassword("admin");
        userDao.save(user);
    }

    @Test
    public void saves() throws Exception {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            User user = new User();
            user.setName("player" + i).setPassword("pass" + i);

            Gender gender = Gender.of(1 + (new Random().nextInt(100)) % 3);
            user.setGender(gender);
            list.add(user);
        }
        userDao.saves(list);
    }

    @Test
    public void find() throws Exception {
        User user = userDao.findById(1L);
        print(user);
    }

    @Test
    public void findList() throws Exception {
        List<User> list = userDao.findList();
        print(list);
    }

    @Test
    public void update() throws Exception {
        User user = userDao.findById(1L);
        user.setName("xyz");
        user.setId(3L);
        user.setUpdateTime(LocalDateTime.now());

        userDao.update(user);
    }

    @Test
    public void update2() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("player-detach");
        user.setPassword("none");
        user.setUpdateTime(LocalDateTime.now().plusDays(1));

        userDao.update(user);
    }

    @Test
    public void update3() throws Exception {
        User user = new User();
        //user.setId(3L);//没有id或者不存在指定id的对象 都会报错
//        user.setId(0L);
        user.setName("player2");
        user.setPassword("go");
        user.setUpdateTime(LocalDateTime.now());

        userDao.update(user);
    }

    @Test
    public void merge() throws Exception {
        User user = new User();
        user.setId(1L);//update3()的情况均为Insert
        user.setName("super");
        user.setPassword("ps");
        user.setUpdateTime(LocalDateTime.now());

        userDao.merge(user);
    }

    @Test
    public void delete() throws Exception {
        System.out.println(userDao.deleteById(9L));
    }

    @Test
    public void delete1() throws Exception {
        User user = new User().setId(9L);
        //userDao.deleteByEntity(user);
        EntityManager manager = userDao.manager();
        System.out.println(manager);
        manager.remove(user);
    }

    @Test
    public void delete2() throws Exception {
        System.out.println(userDao.deleteByIds(Arrays.asList(new Long[]{3L, 9L})));
    }

    @Test
    public void delete3() throws Exception {
        System.out.println(userDao.deleteByIds(new Long[]{5L, 4L, 3L}));
    }

    @Test
    public void delete4() throws Exception {
        List<User> list = userDao.findList();
        userDao.deleteByEntities(list);
    }

    @Test
    public void findName() throws Exception {
        User user = userDao.find("player2", "pass2");
        print(user);
    }

    @Test
    public void findList2() throws Exception {
        List<User> list = userDao.findList("a", Gender.FEMALE, LocalDate.of(2016, 11, 23), LocalDate.of(2017, 12, 14), null, null);
        list.forEach(this::print);

    }

    @Test
    public void findList3() throws Exception {
        Sort sort = Sort.of("name", Rule.DESC);
        Page page = Page.of(1, 3);
        List<User> list = userDao.findList("l", Gender.FEMALE, null, null, page, sort);
        list.forEach(this::print);

    }

    @Test
    public void count() throws Exception {
        long count = userDao.count("l", Gender.MALE, null, null);
        System.out.println(count);
    }

    @Test
    public void count2() throws Exception {
        System.out.println(userDao.count());
    }
}