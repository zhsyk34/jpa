package com.cat.jpa.tool.helper;

public abstract class MultiCallback<R, F> {

   /* public final R find(EntityManager manager, Class<R> result, Class<F> from) {
        assert manager != null && result != null && from != null;
        return this.findList(manager, result, from, null, null).stream().findFirst().orElse(null);
    }

    public final List<R> findList(EntityManager manager, Class<R> result, Class<F> from, Page page, Sort sort) {
        assert manager != null && result != null && from != null;

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<R> query = builder.createQuery(result);
        Root<F> root = query.from(from);

        this.execute(query, root);

        Order order = Orders.from(builder, root, sort);
        if (order != null) {
            query.orderBy(order);
        }

        TypedQuery<R> typedQuery = manager.createQuery(query);
        if (page != null) {
            typedQuery.setFirstResult(page.offset()).setMaxResults(page.size());
        }

        return typedQuery.getResultList();
    }

    public final long count(EntityManager manager, Class<R> result, Class<F> from, Page page, Sort sort) {
        assert manager != null && result != null && from != null;

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<R> query = builder.createQuery(result);
        Root<F> root = query.from(from);

        this.execute(query, root);

        Order order = Orders.from(builder, root, sort);
        if (order != null) {
            query.orderBy(order);
        }

        TypedQuery<R> typedQuery = manager.createQuery(query);
        if (page != null) {
            typedQuery.setFirstResult(page.offset()).setMaxResults(page.size());
        }

        return typedQuery.getResultList();
    }

    protected abstract void execute(CriteriaQuery<R> query, Root<F> root);*/

}
