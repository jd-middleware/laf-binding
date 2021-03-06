package com.jd.laf.binding.reflect.array;

/**
 * 布尔数组
 */
public class PrimitiveBooleanArray implements ArrayObject {

    protected boolean[] array;

    public PrimitiveBooleanArray(int size) {
        array = new boolean[size];
    }

    public PrimitiveBooleanArray(Object array) {
        this.array = (boolean[]) array;
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Boolean) value;
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
