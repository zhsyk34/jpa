package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.ProjectDao;
import com.cat.jpa.entity.Project;
import com.cat.jpa.tool.helper.SingleCount;
import com.cat.jpa.tool.helper.SingleQuery;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Restricts;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ProjectDaoImpl extends CommonDaoImpl<Project, Long> implements ProjectDao {
    @Override
    public Project find(String name) {
        CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
        Root<Project> root = criteria.from(Project.class);
        criteria.where(builder.equal(root.get("name"), name));
        return super.find(manager.createQuery(criteria));
    }

    @Override
    public List<Project> findList(String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        return super.findList(page, sort, new SingleQuery<Project>() {
            @Override
            protected List<Predicate> execute(Root<Project> root) {
                return predicates(root, name, begin, end);
            }
        });
    }

    @Override
    public long count(String name, LocalDate begin, LocalDate end) {
        return super.count(new SingleCount<Project>() {
            @Override
            protected List<Predicate> execute(Root<Project> root) {
                return predicates(root, name, begin, end);
            }
        });
    }

    private List<Predicate> predicates(Root<Project> root, String name, LocalDate begin, LocalDate end) {
        Restricts<Predicate> list = Restricts.instance();
        if (ValidateKit.notEmpty(name)) {
            list.append(builder.like(root.get("name"), "%" + name + "%"));
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
