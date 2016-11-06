package com.cat.jpa.tool.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public final class Sorts {

    public static <T, X> CriteriaQuery<T> sort(CriteriaBuilder builder, CriteriaQuery<T> criteria, Root<X> root, Sort sort) {
        Order order = Orders.from(builder, root, sort);
        if (order != null) {
            criteria.orderBy(order);
        }
        return criteria;
    }
}
