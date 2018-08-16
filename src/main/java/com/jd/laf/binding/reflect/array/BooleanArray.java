package com.jd.laf.binding.reflect.array;

/**
 * 布尔数组
 */
public class BooleanArray implements ArrayObject {

    protected Boolean[] array;

    public BooleanArray(int size) {
        array = new Boolean[size];
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
        return array.length;
    }

    @Override
    public Object getArray() {
        return array;
    }
}
