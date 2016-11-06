package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.BuildDao;
import com.cat.jpa.entity.Build;
import com.cat.jpa.entity.Project;
import com.cat.jpa.tool.helper.QueryCallback;
import com.cat.jpa.tool.helper.SingleCallback;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Restricts;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;
import com.cat.jpa.vo.BuildVO;
import com.google.common.collect.Iterables;
import org.springframework.stereotype.Repository;

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

        CriteriaQuery<Build> criteria = builder.createQuery(Build.class);
        Root<Build> root = criteria.from(Build.class);
        Join<Build, Project> join = root.join("project");
        criteria.where(builder.equal(join.get("id"), projectId), builder.equal(root.get("name"), name));

        return super.find(manager.createQuery(criteria));
    }

    @Override
    public List<Build> findList(Long projectId, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        return super.findList(page, sort, new SingleCallback<Build>() {
            @Override
            protected List<Predicate> restrict(Root<Build> root) {
                return simple(root, root.join("project"), projectId, name, begin, end);
            }
        });
    }

    @Override
    public long count(Long projectId, String name, LocalDate begin, LocalDate end) {
        return super.count(new SingleCallback<Build>() {
            @Override
            protected List<Predicate> restrict(Root<Build> root) {
                return simple(root, root.join("project"), projectId, name, begin, end);
            }
        });
    }

    @Override
    public List<Build> findList(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        return super.findList(page, sort, new SingleCallback<Build>() {
            @Override
            protected List<Predicate> restrict(Root<Build> root) {
                return range(root, ids, name, begin, end);
            }
        });
    }

    @Override
    public long count(Collection<Long> ids, String name, LocalDate begin, LocalDate end) {
        return super.count(new SingleCallback<Build>() {
            @Override
            protected List<Predicate> restrict(Root<Build> root) {
                return range(root, ids, name, begin, end);
            }
        });
    }

    @Override
    public List<BuildVO> findVOList(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        return super.findList(page, sort, BuildVO.class, new QueryCallback<BuildVO, Build>() {
            @Override
            protected <BuildVO> void execute(CriteriaQuery<BuildVO> criteria, Root<Build> root) {
                Join<Build, Project> join = root.join("project");
                criteria.where(Iterables.toArray(range(root, ids, name, begin, end), Predicate.class));
                criteria.multiselect(
                        root.get("id"),
                        root.get("name"),
                        root.get("createTime"),
                        root.get("updateTime"),
                        join.get("id").alias("projectId"),
                        join.get("name").alias("projectName")
                );
            }
        });
    }

    //TODO:CHECK
    /*public List<BuildVO> findVOList_1(Collection<Long> ids, String name, LocalDate begin, LocalDate end, Page page, Sort sort) {
        String sql = "SELECT b.id, b.name, b.createTime, b.updateTime, p.id AS projectId, p.name AS projectName";
        sql += " FROM build b INNER JOIN project p ON b.projectId = p.id";
        sql += " WHERE b.id IN :ids AND b.name LIKE :name";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("ids", ids);
        query.setParameter("name", name);
        org.hibernate.Query q = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(BuildVO.class));
        List<BuildVO> list = q.getResultList();
        return list;
    }*/

    private List<Predicate> simple(Root<Build> root, Join<Build, Project> join, Long projectId, String name, LocalDate begin, LocalDate end) {
        Restricts<Predicate> list = Restricts.instance();
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
        return list.list();
    }

    private List<Predicate> range(Root<Build> root, Collection<Long> ids, String name, LocalDate begin, LocalDate end) {
        Restricts<Predicate> list = Restricts.instance();
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
}
