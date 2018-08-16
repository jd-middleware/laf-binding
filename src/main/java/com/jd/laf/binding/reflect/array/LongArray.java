package com.jd.laf.binding.reflect.array;

/**
 * 长整数数组
 */
public class LongArray implements ArrayObject {

    protected Long[] array;

    public LongArray(int size) {
        array = new Long[size];
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Long) value;
    }

    @Override
    public Object get(final int index) {
        return array[index];
    }

    @Override
    public int length() {
        return array.length;
    }

    @Override
    public Object getArray() {
        return array;
    }
}
