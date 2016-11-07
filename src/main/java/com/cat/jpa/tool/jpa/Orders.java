package com.cat.jpa.tool.jpa;

import com.cat.jpa.tool.kit.ValidateKit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public final class Orders {
	public static <X> Order from(CriteriaBuilder builder, Root<X> root, Sort sort) {
		if (sort == null) {
			return null;
		}
		Rule rule = sort.rule();
		Path<?> path = root.get(sort.field());
		return (rule == Rule.DESC) ? builder.desc(path) : builder.asc(path);
	}

	public static <X> List<Order> from(CriteriaBuilder builder, Root<X> root, List<Sort> sorts) {
		if (ValidateKit.empty(sorts)) {
			return null;
		}
		return sorts.stream().filter(sort -> sort != null).map(sort -> from(builder, root, sort)).collect(Collectors.toList());
	}

}
