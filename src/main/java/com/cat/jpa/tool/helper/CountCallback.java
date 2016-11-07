package com.cat.jpa.tool.helper;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class CountCallback<T> {

	public final long count(EntityManager manager, Class<T> from) {
		assert manager != null && from != null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<T> root = criteria.from(from);
		criteria.select(builder.count(root));

		this.execute(criteria, root);

		return manager.createQuery(criteria).getSingleResult();
	}

	/**
	 * in here,you can set
	 * 1.the join search,2.the select from,3.the conditions
	 */
	protected abstract void execute(CriteriaQuery<Long> criteria, Root<T> root);
}
