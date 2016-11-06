package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.UnitDao;
import com.cat.jpa.entity.Build;
import com.cat.jpa.entity.Unit;
import com.cat.jpa.tool.helper.QueryCallback;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Restricts;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;
import com.cat.jpa.vo.UnitVO;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

@Repository
public class UnitDaoImpl extends CommonDaoImpl<Unit, Long> implements UnitDao {

    @Override
    public Unit find(Long buildId, String name) {
        assert ValidateKit.valid(buildId) && ValidateKit.notEmpty(name);

        CriteriaQuery<Unit> query = builder.createQuery(Unit.class);
        Root<Unit> root = query.from(Unit.class);
        Join<Unit, Build> join = root.join("build");
        query.where(builder.equal(join.get("id"), buildId), builder.equal(root.get("name"), name));

        return super.find(manager.createQuery(query));
    }

    @Override
    public List<Unit> findList(Long buildId, String name, Page page, Sort sort) {
        return super.findList(page, sort, new QueryCallback<Unit, Unit>() {
            @Override
            protected <R> void execute(CriteriaQuery<R> criteria, Root<Unit> root) {
                criteria.where(simple(root, root.join("build"), buildId, name));
            }
        });
    }

    @Override
    public long count(Long buildId, String name) {
        return super.count();
    }

    @Override
    public List<Unit> findList(Collection<Long> buildIds, String projectName, String buildName, String name, Page page, Sort sort) {
        return null;
    }

    @Override
    public long count(Collection<Long> buildIds, String projectName, String buildName, String name) {
        return 0;
    }

    @Override
    public List<UnitVO> findVOList(Collection<Long> buildIds, String projectName, String buildName, String name, Page page, Sort sort) {
        return null;
    }

    private Predicate[] simple(Root<Unit> root, Join<Unit, Build> join, Long buildId, String name) {
        Restricts<Predicate> list = Restricts.instance();
        if (ValidateKit.valid(buildId)) {
            list.append(builder.equal(join.get("id"), buildId));
        }
        if (ValidateKit.notEmpty(name)) {
            list.append(builder.like(root.get("name"), "%" + name + "%"));
        }
        return list.array(Predicate.class);
    }
}
