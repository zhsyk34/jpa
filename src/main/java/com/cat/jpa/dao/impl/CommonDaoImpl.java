package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.CommonDao;
import com.cat.jpa.tool.helper.CountCallback;
import com.cat.jpa.tool.helper.QueryCallback;
import com.cat.jpa.tool.helper.SingleCount;
import com.cat.jpa.tool.helper.SingleQuery;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Rule;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.jpa.Sorts;
import com.cat.jpa.tool.kit.ReflectKit;
import com.cat.jpa.tool.kit.ValidateKit;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CommonDaoImpl<E, K extends Serializable> implements CommonDao<E, K> {

    private final Class<E> entityClass;
    private final Class<K> idClass;
    protected CriteriaBuilder builder;
    protected String idColumn;
    @PersistenceContext
    protected EntityManager manager;

    protected CommonDaoImpl() {
        entityClass = ReflectKit.getGenericType(this.getClass(), 0);
        idClass = ReflectKit.getGenericType(this.getClass(), 1);
        assert entityClass != null && idClass != null;
    }

    @PostConstruct
    protected final void init() {
        builder = manager.getCriteriaBuilder();

        Metamodel metamodel = manager.getMetamodel();
        EntityType<E> entityType = metamodel.entity(entityClass);
        assert entityType.getIdType().getJavaType() == idClass;
        idColumn = entityType.getId(idClass).getName();
        assert ValidateKit.notEmpty(idColumn);
    }

    @Override
    public final EntityManager manager() {
        return manager;
    }

    @Override
    public final CriteriaBuilder builder() {
        return builder;
    }

    @Override
    public final K id(E e) {
        EntityManagerFactory factory = manager.getEntityManagerFactory();
        @SuppressWarnings("unchecked")
        K id = (K) factory.getPersistenceUnitUtil().getIdentifier(e);
        return id;
    }

    @Override
    public final void save(E e) {
        assert e != null;
        manager.persist(e);
    }

    @Override
    public final void saves(Collection<E> es) {
        assert ValidateKit.notEmpty(es);
        es.forEach(this::save);
    }

    @Override
    public final int deleteById(K k) {
        assert k != null;

        CriteriaDelete<E> criteria = builder.createCriteriaDelete(entityClass);
        criteria.where(builder.equal(criteria.from(entityClass).get(idColumn), k));

        return manager.createQuery(criteria).executeUpdate();
    }

    @Override
    public final int deleteByIds(K[] ks) {
        if (ValidateKit.empty(ks)) {
            return 0;
        }
        return this.deleteByIds(Arrays.asList(ks));
    }

    @Override
    public final int deleteByIds(Collection<K> ks) {
        if (ValidateKit.empty(ks)) {
            return 0;
        }
        CriteriaDelete<E> criteria = builder.createCriteriaDelete(entityClass);
        criteria.where(criteria.from(entityClass).get(idColumn).in(ks));
        return manager.createQuery(criteria).executeUpdate();
    }

    @Override
    public final int deleteByEntity(E e) {
        assert e != null;
        return this.deleteById(id(e));
    }

    @Override
    public final int deleteByEntities(Collection<E> es) {
        assert ValidateKit.notEmpty(es);
        Collection<K> ks = new ArrayList<>();
        es.forEach(e -> ks.add(id(e)));
        return this.deleteByIds(ks);
    }

    @Override
    public final long deleteAll() {
        CriteriaDelete<E> criteria = builder.createCriteriaDelete(entityClass);
        criteria.from(entityClass);
        return manager.createQuery(criteria).executeUpdate();
    }

    @Override
    public final void update(E e) {
        assert e != null;
        K k = id(e);
        assert k != null;
        if (k instanceof Long) {
            assert (Long) k > 0;
        }
        if (k instanceof Integer) {
            assert (Integer) k > 0;
        }
        this.merge(e);
    }

    @Override
    public final void merge(E e) {
        assert e != null;
        manager.merge(e);
    }

    @Override
    public final boolean contains(E e) {
        return e != null && manager.contains(e);
    }

    @Override
    public final E findById(K k) {
        assert k != null;
        return manager.find(entityClass, k);
    }

    @Override
    public final List<E> findList() {
        CriteriaQuery<E> criteria = builder().createQuery(entityClass);
        criteria.from(entityClass);
        return manager.createQuery(criteria).getResultList();
    }

    @Override
    public final long count() {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        criteria.select(builder.count(criteria.from(entityClass)));
        return manager.createQuery(criteria).getSingleResult();
    }

    /*------------------the following is setting options for criteria------------------*/
    public final <T, X> CriteriaQuery<T> sort(CriteriaQuery<T> criteria, Root<X> root, Sort sort) {
        sort = sort == null ? Sort.of(idColumn, Rule.DESC) : sort;
        return Sorts.sort(builder, criteria, root, sort);
    }

    /*------------------the following is setting query before return------------------*/

    public final <T> T find(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public final <T> List<T> findList(TypedQuery<T> query, Page page) {
        if (page != null) {
            query.setFirstResult(page.offset()).setMaxResults(page.size());
        }
        return query.getResultList();
    }

    /*------------------the following is single query template by callback------------------*/

    public final E find(SingleQuery<E> callback) {
        return callback.find(manager, entityClass);
    }

    public final List<E> findList(Page page, List<Sort> sorts, SingleQuery<E> callback) {
        return callback.findList(manager, entityClass, page, sorts);
    }

    public final List<E> findList(Page page, Sort sort, SingleQuery<E> callback) {
        return callback.findList(manager, entityClass, page, sort);
    }

    public final long count(SingleCount<E> callback) {
        return callback.count(manager, entityClass);
    }

    /*------------------the following is multi query template by callback------------------*/

    public final <R> R find(Class<R> result, QueryCallback<R, E> callback) {
        return callback.find(manager, result, entityClass);
    }

    public final <R> List<R> findList(Page page, List<Sort> sorts, Class<R> result, QueryCallback<R, E> callback) {
        return callback.findList(manager, result, entityClass, page, sorts);
    }

    public final <R> List<R> findList(Page page, Sort sort, Class<R> result, QueryCallback<R, E> callback) {
        return callback.findList(manager, result, entityClass, page, sort);
    }

    //count(F),allow E base
    @Deprecated
    public final <F> long count(Class<F> clazz, CountCallback<F> callback) {
        return callback.count(manager, clazz);
    }

}
