package com.cat.jpa.tool.jpa;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

public class Conditions<T> {

    private final List<T> list = new ArrayList<>();

    private Conditions() {
    }

    public static <T> Conditions<T> instance() {
        return new Conditions<>();
    }

    public Conditions append(T t) {
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
