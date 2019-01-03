package com.jd.laf.binding.util;

import java.util.Iterator;

/**
 * 类及超类迭代器，截止到Object.class结束
 */
public class SuperClassIterator implements Iterator<Class> {

    protected Class clazz;

    public SuperClassIterator(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean hasNext() {
        return clazz != null && !clazz.equals(Object.class);
    }

    @Override
    public Class next() {
        Class result = clazz;
        clazz = clazz.getSuperclass();
        return result;
    }

    @Override
    public void remove() {

    }
}
