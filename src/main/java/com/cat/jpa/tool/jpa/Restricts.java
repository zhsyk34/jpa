package com.cat.jpa.tool.jpa;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

public final class Restricts<T> {

	private final List<T> list = new ArrayList<>();

	private Restricts() {
	}

	public static <T> Restricts<T> instance() {
		return new Restricts<>();
	}

	public Restricts append(T t) {
		list.add(t);
		return this;
	}

	public List<T> list() {
		return list;
	}

	public T[] array(Class<T> clazz) {
		return Iterables.toArray(list, clazz);
	}
}
