package com.jd.laf.binding.reflect.array;

/**
 * 单精度浮点数数组
 */
public class FloatArray implements ArrayObject {

    protected Float[] array;

    public FloatArray(int size) {
        array = new Float[size];
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Float) value;
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
