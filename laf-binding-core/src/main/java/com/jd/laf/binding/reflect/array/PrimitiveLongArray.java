package com.jd.laf.binding.reflect.array;

/**
 * 长整数数组
 */
public class PrimitiveLongArray implements ArrayObject {

    protected long[] array;

    public PrimitiveLongArray(int size) {
        array = new long[size];
    }

    public PrimitiveLongArray(Object array) {
        this.array = (long[]) array;
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
        return array == null ? 0 : array.length;
    }

    @Override
    public Object getArray() {
        return array;
    }
}
