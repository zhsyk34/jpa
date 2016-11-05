package com.cat.jpa.tool.jpa;

import com.cat.jpa.tool.kit.ValidateKit;
import com.google.common.collect.Lists;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

public class Orders {

    public static <E> Order from(CriteriaBuilder builder, Root<E> root, Sort sort) {
        if (sort == null) {
            return null;//sort = Sort.of(idColumn, null);
        }
        Rule rule = sort.getRule();
        Path<?> path = root.get(sort.getColumn());
        return (rule == Rule.DESC) ? builder.desc(path) : builder.asc(path);
    }

    public static <E> List<Order> from(CriteriaBuilder builder, Root<E> root, List<Sort> sorts) {
        if (ValidateKit.empty(sorts)) {
            return null;
        }
        List<Order> list = Lists.newArrayList();
        sorts.forEach(sort -> list.add(from(builder, root, sort)));
        return list;
    }
}
