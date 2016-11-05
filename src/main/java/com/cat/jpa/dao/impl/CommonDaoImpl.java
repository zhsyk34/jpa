package com.cat.jpa.dao.impl;

import com.cat.jpa.dao.CommonDao;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.QueryCallback;
import com.cat.jpa.tool.jpa.Sort;
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

        CriteriaDelete<E> query = builder.createCriteriaDelete(entityClass);
        query.where(builder.equal(query.from(entityClass).get(idColumn), k));

        return manager.createQuery(query).executeUpdate();
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
        CriteriaDelete<E> query = builder.createCriteriaDelete(entityClass);
        query.where(query.from(entityClass).get(idColumn).in(ks));
        return manager.createQuery(query).executeUpdate();
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
        CriteriaDelete<E> query = builder.createCriteriaDelete(entityClass);
        query.from(entityClass);
        return manager.createQuery(query).executeUpdate();
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
        CriteriaQuery<E> query = builder().createQuery(entityClass);
        query.from(entityClass);
        return manager.createQuery(query).getResultList();
    }

    @Override
    public final long count() {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(entityClass)));
        return manager.createQuery(query).getSingleResult();
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

    /*------------------the following is query template by callback------------------*/

    public final E find(QueryCallback<E> callback) {
        return callback.find(manager, entityClass);
    }

    public final List<E> findList(Page page, Sort sort, QueryCallback<E> callback) {
        return callback.findList(manager, entityClass, page, sort);
    }

    public final List<E> findList(Page page, List<Sort> sorts, QueryCallback<E> callback) {
        return callback.findList(manager, entityClass, page, sorts);
    }

    public final long count(QueryCallback<E> callback) {
        return callback.count(manager, entityClass);
    }

}
