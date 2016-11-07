package com.cat.jpa.tool.helper;

import com.cat.jpa.tool.kit.ValidateKit;
import com.google.common.collect.Iterables;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class SingleCount<T> extends CountCallback<T> {
	@Override
	protected final void execute(CriteriaQuery<Long> criteria, Root<T> root) {
		List<Predicate> list = this.execute(root);
		if (ValidateKit.notEmpty(list)) {
			criteria.where(Iterables.toArray(list, Predicate.class));
		}
	}

	protected abstract List<Predicate> execute(Root<T> root);

}
