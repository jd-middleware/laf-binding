package com.jd.laf.binding.reflect.array;

/**
 * 短整数数组
 */
public class ShortArray implements ArrayObject {

    protected Short[] array;

    public ShortArray(int size) {
        array = new Short[size];
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Short) value;
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
