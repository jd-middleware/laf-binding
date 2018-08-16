package com.jd.laf.binding.reflect.array;

/**
 * 双精度浮点数数组
 */
public class DoubleArray implements ArrayObject {

    protected Double[] array;

    public DoubleArray(int size) {
        array = new Double[size];
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Double) value;
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
