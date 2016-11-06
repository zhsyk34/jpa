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
 * select single entity
 *
 * @param <T>
 */
public abstract class SingleCallback<T> extends QueryCallback<T, T> {

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
    protected <R> void execute(CriteriaQuery<R> criteria, Root<T> root) {
        List<Predicate> list = this.restrict(root);
        if (ValidateKit.notEmpty(list)) {
            criteria.where(Iterables.toArray(list, Predicate.class));
        }
    }

    protected abstract List<Predicate> restrict(Root<T> root);
}
