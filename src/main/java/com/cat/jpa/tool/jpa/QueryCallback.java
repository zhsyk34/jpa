package com.cat.jpa.tool.jpa;

import com.cat.jpa.tool.kit.ValidateKit;
import com.google.common.collect.Iterables;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

public abstract class QueryCallback<E> {

    public final E find(EntityManager manager, Class<E> clazz) {
        assert manager != null && clazz != null;
        return this.findList(manager, clazz, null, (Sort) null).stream().findFirst().orElse(null);
    }

    public final List<E> findList(EntityManager manager, Class<E> clazz, Page page, Sort sort) {
        assert manager != null && clazz != null;

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(clazz);
        Root<E> root = query.from(clazz);

        //conditions
        Collection<Predicate> predicates = this.execute(root);
        if (ValidateKit.notEmpty(predicates)) {
            query.where(Iterables.toArray(predicates, Predicate.class));
        }

        //sort
        if (sort != null) {
            query.orderBy(Orders.from(builder, root, sort));
        }

        //page
        TypedQuery<E> typedQuery = manager.createQuery(query);
        if (page != null) {
            typedQuery.setFirstResult(page.offset()).setMaxResults(page.size());
        }

        return typedQuery.getResultList();
    }

    public final List<E> findList(EntityManager manager, Class<E> clazz, Page page, List<Sort> sorts) {
        assert manager != null && clazz != null;

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(clazz);
        Root<E> root = query.from(clazz);

        Collection<Predicate> predicates = this.execute(root);
        if (ValidateKit.notEmpty(predicates)) {
            query.where(Iterables.toArray(predicates, Predicate.class));
        }

        List<Order> orders = Orders.from(builder, root, sorts);
        if (ValidateKit.notEmpty(orders)) {
            query.orderBy(Iterables.toArray(orders, Order.class));
        }

        TypedQuery<E> typedQuery = manager.createQuery(query);
        if (page != null) {
            typedQuery.setFirstResult(page.offset()).setMaxResults(page.size());
        }

        return typedQuery.getResultList();
    }

    public long count(EntityManager manager, Class<E> clazz) {
        assert manager != null && clazz != null;
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<E> root = query.from(clazz);

        query.select(builder.count(root));

        Collection<Predicate> predicates = this.execute(root);
        if (ValidateKit.notEmpty(predicates)) {
            query.where(Iterables.toArray(predicates, Predicate.class));
        }

        return manager.createQuery(query).getSingleResult();
    }

    protected abstract List<Predicate> execute(Root<E> root);
}
