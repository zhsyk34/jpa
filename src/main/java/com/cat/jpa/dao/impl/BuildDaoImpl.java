package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.BuildDao;
import com.cat.jpa.entity.Build;
import com.cat.jpa.entity.Project;
import com.cat.jpa.tool.jpa.*;
import com.cat.jpa.tool.kit.ValidateKit;
import com.cat.jpa.vo.BuildVO;
import com.google.common.collect.Iterables;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public class BuildDaoImpl extends CommonDaoImpl<Build, Long> implements BuildDao {
    @Override
    public Build find(Long projectId, String name) {
        assert ValidateKit.valid(projectId) && ValidateKit.notEmpty(name);

        CriteriaQuery<Build> query = builder.createQuery(Build.class);
        Root<Build> root = query.from(Build.class);
        Join<Build, Project> join = root.join("project");
        query.where(builder.equal(join.get("id"), projectId), builder.equal(root.get("name"), name));

        return super.find(manager.createQuery(query));
    }

    private Predicate[] simple(Root<Build> root, Join<Build, Project> join, Long projectId, String name, LocalDate begin, LocalDate end) {
        Conditions<Predicate> list = Conditions.instance();
        if (ValidateKit.valid(projectId)) {
            list.append(builder.equal(join.get("id"), projectId));
        }
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
        return list.array(Predicate.class);
    }

    @Override
    public List<Build> findList(Long projectId, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        CriteriaQuery<Build> query = builder.createQuery(Build.class);
        Root<Build> root = query.from(Build.class);
        Join<Build, Project> join = root.join("project");

        query.where(simple(root, join, projectId, name, begin, end));
        Order order = Orders.from(builder, root, sort);
        if (order != null) {
            query.orderBy(order);
        }

        return super.findList(manager.createQuery(query), page);
    }

    @Override
    public long count(Long projectId, String name, LocalDate begin, LocalDate end) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Build> root = query.from(Build.class);
        Join<Build, Project> join = root.join("project");

        query.where(simple(root, join, projectId, name, begin, end));
        query.select(builder.count(root));

        return manager.createQuery(query).getSingleResult();
    }

    private List<Predicate> range(Root<Build> root, Collection<Long> ids, String name, LocalDate begin, LocalDate end) {
        Conditions<Predicate> list = Conditions.instance();
        if (ValidateKit.notEmpty(ids)) {
            list.append(root.get("id").in(ids));
        }
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

    @Override
    public List<Build> findList(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        return super.findList(page, sort, new QueryCallback<Build>() {
            @Override
            protected List<Predicate> execute(Root<Build> root) {
                return range(root, ids, name, begin, end);
            }
        });
    }

    @Override
    public long count(Collection<Long> ids, String name, LocalDate begin, LocalDate end) {
        return super.count(new QueryCallback<Build>() {
            @Override
            protected List<Predicate> execute(Root<Build> root) {
                return range(root, ids, name, begin, end);
            }
        });
    }

    //    @Override2
    public List<BuildVO> findVOList2(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        CriteriaQuery<BuildVO> query = builder.createQuery(BuildVO.class);
        Root<Build> root = query.from(Build.class);
        Join<Build, Project> join = root.join("project");

        query.where(Iterables.toArray(range(root, ids, name, begin, end), Predicate.class));

        query.multiselect(
                root.get("id"),
                root.get("name"),
                root.get("createTime"),
                root.get("updateTime"),
                join.get("id").alias("projectId"),
                join.get("name").alias("projectName")
        );

        Order order = Orders.from(builder, root, sort);
        if (order != null) {
            query.orderBy(order);
        }

        return super.findList(manager.createQuery(query), page);
    }

    @Override
    public List<BuildVO> findVOList(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        Query query = manager.createNativeQuery("select b.id,b.name,b.createTime,b.updateTime,p.id as projectId,p.name as projectName from build b INNER JOIN project p");
        org.hibernate.Query q = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(BuildVO.class));
        List<BuildVO> list = q.getResultList();
        return list;
    }

}
