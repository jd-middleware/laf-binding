package com.jd.laf.binding.reflect.array;

import java.lang.reflect.Array;

/**
 * 动态反射创建数组
 */
public class DynamicArray implements ArrayObject {

    protected Object array;
    protected int size;

    public DynamicArray(Class<?> clazz, int size) {
        array = java.lang.reflect.Array.newInstance(clazz, size);
    }

    public DynamicArray(Object array) {
        this.array = array;
        this.size = array == null ? 0 : Array.getLength(array);
    }

    @Override
    public void set(final int index, final Object value) {
        java.lang.reflect.Array.set(array, index, value);
    }

    @Override
    public Object get(final int index) {
        return java.lang.reflect.Array.get(array, index);
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public Object getArray() {
        return array;
    }
}
