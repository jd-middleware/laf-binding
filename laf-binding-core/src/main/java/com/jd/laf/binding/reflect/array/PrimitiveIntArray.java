package com.jd.laf.binding.reflect.array;

/**
 * 基本类型-整数数组
 */
public class PrimitiveIntArray implements ArrayObject {

    protected int[] array;

    public PrimitiveIntArray(int size) {
        array = new int[size];
    }

    public PrimitiveIntArray(Object array) {
        this.array = (int[]) array;
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
