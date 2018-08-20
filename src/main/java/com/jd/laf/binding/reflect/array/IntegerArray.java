package com.jd.laf.binding.reflect.array;

/**
 * 整数数组
 */
public class IntegerArray implements ArrayObject {

    protected Integer[] array;

    public IntegerArray(int size) {
        array = new Integer[size];
    }

    public IntegerArray(Object array) {
        this.array = (Integer[]) array;
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Integer) value;
    }

    @Override
    public Object get(final int index) {
        return array[index];
    }

    @Override
    public int length() {
        return array == null ? 0 : array.length;
    }

    @Override
    public Object getArray() {
        return array;
    }
}
