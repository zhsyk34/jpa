package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.UserDao;
import com.cat.jpa.entity.User;
import com.cat.jpa.entity.dict.Gender;
import com.cat.jpa.tool.helper.SingleCallback;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Restricts;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class UserDaoImpl extends CommonDaoImpl<User, Long> implements UserDao {
    @Override
    public User find(String name, String password) {
        return super.find(new SingleCallback<User>() {
            @Override
            protected List<Predicate> restrict(Root<User> root) {
                Restricts<Predicate> list = Restricts.instance();
                list.append(builder.equal(root.get("name"), name));
                list.append(builder.equal(root.get("password"), password));
                return list.list();
            }
        });
    }

    @Override
    public List<User> findList(String name, Gender gender, LocalDate begin, LocalDate end, Page page, Sort sort) {
        return super.findList(page, sort, new SingleCallback<User>() {
            @Override
            protected List<Predicate> restrict(Root<User> root) {
                return predicates(root, name, gender, begin, end);
            }
        });
    }

    @Override
    public long count(String name, Gender gender, LocalDate begin, LocalDate end) {
        return super.count(new SingleCallback<User>() {
            @Override
            protected List<Predicate> restrict(Root<User> root) {
                return predicates(root, name, gender, begin, end);
            }
        });
    }

    private List<Predicate> predicates(Root<User> root, String name, Gender gender, LocalDate begin, LocalDate end) {
        Restricts<Predicate> list = Restricts.instance();
        if (ValidateKit.notEmpty(name)) {
            list.append(builder.like(root.get("name"), "%" + name + "%"));
        }
        if (gender != null) {
            list.append(builder.equal(root.get("gender"), gender));
        }
        Path<LocalDateTime> createPath = root.get("createTime");
        if (begin != null) {
            list.append(builder.greaterThanOrEqualTo(createPath, LocalDateTime.of(begin, LocalTime.MIN)));
        }
        if (end != null) {
            list.append(builder.lessThanOrEqualTo(createPath, LocalDateTime.of(end, LocalTime.MIN)));
        }
        return list.list();
    }

}
