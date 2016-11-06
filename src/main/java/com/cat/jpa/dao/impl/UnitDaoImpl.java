package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.UnitDao;
import com.cat.jpa.entity.Build;
import com.cat.jpa.entity.Project;
import com.cat.jpa.entity.Unit;
import com.cat.jpa.tool.helper.QueryCallback;
import com.cat.jpa.tool.helper.SingleCount;
import com.cat.jpa.tool.helper.SingleQuery;
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
        return super.findList(page, sort, new SingleQuery<Unit>() {
            @Override
            protected List<Predicate> execute(Root<Unit> root) {
                return simple(root, root.join("build"), buildId, name);
            }
        });
    }

    @Override
    public long count(Long buildId, String name) {
        return super.count(new SingleCount<Unit>() {
            @Override
            protected List<Predicate> execute(Root<Unit> root) {
                return simple(root, root.join("build"), buildId, name);
            }
        });
    }

    @Override
    public List<Unit> findList(Collection<Long> buildIds, String projectName, String buildName, String name, Page page, Sort sort) {
        return super.findList(page, sort, new SingleQuery<Unit>() {
            @Override
            protected List<Predicate> execute(Root<Unit> root) {
                return range(root, buildIds, projectName, buildName, name);
            }
        });
    }

    @Override
    public long count(Collection<Long> buildIds, String projectName, String buildName, String name) {
        return super.count(new SingleCount<Unit>() {
            @Override
            protected List<Predicate> execute(Root<Unit> root) {
                return range(root, buildIds, projectName, buildName, name);
            }
        });
    }

    //TODO join twice
    @Override
    public List<UnitVO> findVOList(Collection<Long> buildIds, String projectName, String buildName, String name, Page page, Sort sort) {
        return super.findList(page, sort, UnitVO.class, new QueryCallback<UnitVO, Unit>() {
            @Override
            protected void execute(CriteriaQuery<UnitVO> criteria, Root<Unit> root) {
                Join<Unit, Build> buildJoin = root.join("build");
                Join<Build, Project> projectJoin = buildJoin.join("project");
                /*criteria.select(
                        builder.construct(
                                UnitVO.class,
                                root.get("id"),
                                root.get("name"),
                                root.get("createTime"),
                                root.get("updateTime"),
                                buildJoin.get("id").alias("buildId"),
                                buildJoin.get("name").alias("buildName"),
                                projectJoin.get("id").alias("projectId"),
                                projectJoin.get("name").alias("projectName"))

                );*/
                criteria.multiselect(
                        root.get("id"),
                        root.get("name"),
                        root.get("createTime"),
                        root.get("updateTime"),
                        buildJoin.get("id").alias("buildId"),
                        buildJoin.get("name").alias("buildName"),
                        projectJoin.get("id").alias("projectId"),
                        projectJoin.get("name").alias("projectName")
                );
                //criteria.where(Iterables.toArray(range(root, buildIds, projectName, buildName, name), Predicate.class));
            }
        });
    }

    private List<Predicate> range(Root<Unit> unitRoot, Collection<Long> buildIds, String projectName, String buildName, String name) {
        Join<Unit, Build> buildJoin = unitRoot.join("build");
        Join<Build, Project> projectJoin = buildJoin.join("project");

        Restricts<Predicate> list = Restricts.instance();
        if (ValidateKit.notEmpty(buildIds)) {
            list.append(buildJoin.get("id").in(buildIds));
        }
        if (ValidateKit.notEmpty(projectName)) {
            list.append(builder.like(projectJoin.get("name"), "%" + projectName + "%"));
        }
        if (ValidateKit.notEmpty(buildName)) {
            list.append(builder.like(buildJoin.get("name"), "%" + buildName + "%"));
        }
        if (ValidateKit.notEmpty(name)) {
            list.append(builder.like(unitRoot.get("name"), "%" + name + "%"));
        }
        return list.list();
    }

    private List<Predicate> simple(Root<Unit> root, Join<Unit, Build> join, Long buildId, String name) {
        Restricts<Predicate> list = Restricts.instance();
        if (ValidateKit.valid(buildId)) {
            list.append(builder.equal(join.get("id"), buildId));
        }
        if (ValidateKit.notEmpty(name)) {
            list.append(builder.like(root.get("name"), "%" + name + "%"));
        }
        return list.list();
    }
}
