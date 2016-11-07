package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.HouseDao;
import com.cat.jpa.entity.Build;
import com.cat.jpa.entity.House;
import com.cat.jpa.entity.Project;
import com.cat.jpa.entity.Unit;
import com.cat.jpa.tool.helper.QueryCallback;
import com.cat.jpa.tool.helper.SingleCount;
import com.cat.jpa.tool.helper.SingleQuery;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Restricts;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;
import com.cat.jpa.vo.HouseVO;
import com.google.common.collect.Iterables;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

@Repository
public class HouseDaoImpl extends CommonDaoImpl<House, Long> implements HouseDao {
	@Override
	public House find(Long unitId, String name) {
		assert ValidateKit.valid(unitId) && ValidateKit.notEmpty(name);

		CriteriaQuery<House> query = builder.createQuery(House.class);
		Root<House> root = query.from(House.class);
		Join<House, Unit> join = root.join("unit");
		query.where(builder.equal(join.get("id"), unitId), builder.equal(root.get("name"), name));

		return super.find(manager.createQuery(query));
	}

	@Override
	public List<House> findList(Long buildId, Long unitId, String name, Page page, Sort sort) {
		return super.findList(page, sort, new SingleQuery<House>() {
			@Override
			protected List<Predicate> execute(Root<House> root) {
				return simple(root, buildId, unitId, name);
			}
		});
	}

	@Override
	public long count(Long buildId, Long unitId, String name) {
		return super.count(new SingleCount<House>() {
			@Override
			protected List<Predicate> execute(Root<House> root) {
				return simple(root, buildId, unitId, name);
			}
		});
	}

	private List<Predicate> range(Root<House> root, Join<House, Unit> unitJoin, Join<Unit, Build> buildJoin, Join<Build, Project> projectJoin, Collection<Long> buildIds, String projectName, String buildName, String unitName, String name) {
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
		if (ValidateKit.notEmpty(unitName)) {
			list.append(builder.like(unitJoin.get("name"), "%" + unitName + "%"));
		}
		if (ValidateKit.notEmpty(name)) {
			list.append(builder.like(root.get("name"), "%" + name + "%"));
		}
		return list.list();
	}

	@Override
	public List<House> findList(Collection<Long> buildIds, String projectName, String buildName, String unitName, String name, Page page, Sort sort) {
		return super.findList(page, sort, new SingleQuery<House>() {
			@Override
			protected List<Predicate> execute(Root<House> root) {
				Join<House, Unit> unitJoin = root.join("unit");
				Join<Unit, Build> buildJoin = unitJoin.join("build");
				Join<Build, Project> projectJoin = buildJoin.join("project");
				return range(root, unitJoin, buildJoin, projectJoin, buildIds, projectName, buildName, unitName, name);
			}
		});
	}

	@Override
	public long count(Collection<Long> buildIds, String projectName, String buildName, String unitName, String name) {
		return super.count(new SingleCount<House>() {
			@Override
			protected List<Predicate> execute(Root<House> root) {
				Join<House, Unit> unitJoin = root.join("unit");
				Join<Unit, Build> buildJoin = unitJoin.join("build");
				Join<Build, Project> projectJoin = buildJoin.join("project");
				return range(root, unitJoin, buildJoin, projectJoin, buildIds, projectName, buildName, unitName, name);
			}
		});
	}

	@Override
	public List<HouseVO> findVOList(Collection<Long> buildIds, String projectName, String buildName, String unitName, String name, Page page, Sort sort) {
		return super.findList(page, sort, HouseVO.class, new QueryCallback<HouseVO, House>() {
			@Override
			protected void execute(CriteriaQuery<HouseVO> criteria, Root<House> root) {
				Join<House, Unit> unitJoin = root.join("unit");
				Join<Unit, Build> buildJoin = unitJoin.join("build");
				Join<Build, Project> projectJoin = buildJoin.join("project");

				criteria.multiselect(
						root.get("id"),
						root.get("name"),
						root.get("createTime"),
						root.get("updateTime"),
						unitJoin.get("id").alias("unitId"),
						unitJoin.get("name").alias("unitName"),
						buildJoin.get("id").alias("buildId"),
						buildJoin.get("name").alias("buildName"),
						projectJoin.get("id").alias("projectId"),
						projectJoin.get("name").alias("projectName")
				);
				criteria.where(Iterables.toArray(range(root, unitJoin, buildJoin, projectJoin, buildIds, projectName, buildName, unitName, name), Predicate.class));
			}
		});
	}

	private List<Predicate> simple(Root<House> root, Long buildId, Long unitId, String name) {
		Join<House, Unit> unitJoin = root.join("unit");
		Join<Unit, Build> buildJoin = unitJoin.join("build");

		Restricts<Predicate> list = Restricts.instance();
		if (ValidateKit.valid(buildId)) {
			list.append(builder.equal(buildJoin.get("id"), buildId));
		}
		if (ValidateKit.valid(unitId)) {
			list.append(builder.equal(unitJoin.get("id"), unitId));
		}
		if (ValidateKit.notEmpty(name)) {
			list.append(builder.like(root.get("name"), "%" + name + "%"));
		}
		return list.list();
	}

}
