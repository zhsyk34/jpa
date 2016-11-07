package com.cat.jpa.tool.helper;

import com.cat.jpa.tool.jpa.Orders;
import com.cat.jpa.tool.jpa.Page;
import com.cat.jpa.tool.jpa.Sort;
import com.cat.jpa.tool.kit.ValidateKit;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @param <R> the return type class
 * @param <F> the select from entity class
 */
public abstract class QueryCallback<R, F> {

	public final R find(EntityManager manager, Class<R> result, Class<F> from) {
		assert manager != null && result != null && from != null;
		return this.findList(manager, result, from, null, (Sort) null).stream().findFirst().orElse(null);
	}

	public final List<R> findList(EntityManager manager, Class<R> result, Class<F> from, Page page, List<Sort> sorts) {
		assert manager != null && result != null && from != null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<R> criteria = builder.createQuery(result);
		Root<F> root = criteria.from(from);

		this.execute(criteria, root);

		List<Order> orders = Orders.from(builder, root, sorts);
		if (ValidateKit.notEmpty(orders)) {
			criteria.orderBy(orders);
		}

		TypedQuery<R> query = manager.createQuery(criteria);
		if (page != null) {
			query.setFirstResult(page.offset()).setMaxResults(page.size());
		}

		return query.getResultList();
	}

	public final List<R> findList(EntityManager manager, Class<R> result, Class<F> from, Page page, Sort sort) {
		//return this.findList(manager, result, from, page, Collections.singletonList(sort));
		assert manager != null && result != null && from != null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<R> criteria = builder.createQuery(result);
		Root<F> root = criteria.from(from);

		this.execute(criteria, root);

		Order order = Orders.from(builder, root, sort);
		if (order != null) {
			criteria.orderBy(order);
		}

		TypedQuery<R> query = manager.createQuery(criteria);
		if (page != null) {
			query.setFirstResult(page.offset()).setMaxResults(page.size());
		}

		return query.getResultList();
	}

	/**
	 * in here,you can set
	 * 1.the join search,2.the select from,3.the conditions
	 */
	protected abstract void execute(CriteriaQuery<R> criteria, Root<F> root);

}
