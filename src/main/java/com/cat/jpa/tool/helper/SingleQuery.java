package com.cat.jpa.tool.helper;

import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;
import com.google.common.collect.Iterables;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * select from single entity and return it
 *
 * @param <T>
 */
public abstract class SingleQuery<T> extends QueryCallback<T, T> {

    public final T find(EntityManager manager, Class<T> clazz) {
        return super.find(manager, clazz, clazz);
    }

    public final List<T> findList(EntityManager manager, Class<T> clazz, Page page, List<Sort> sorts) {
        return super.findList(manager, clazz, clazz, page, sorts);
    }

    public final List<T> findList(EntityManager manager, Class<T> clazz, Page page, Sort sort) {
        return super.findList(manager, clazz, clazz, page, sort);
    }

    @Override
    protected final void execute(CriteriaQuery<T> criteria, Root<T> root) {
        List<Predicate> list = this.execute(root);
        if (ValidateKit.notEmpty(list)) {
            criteria.where(Iterables.toArray(list, Predicate.class));
        }
    }

    protected abstract List<Predicate> execute(Root<T> root);
}
