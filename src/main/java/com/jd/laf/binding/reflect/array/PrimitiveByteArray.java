package com.jd.laf.binding.reflect.array;

/**
 * 字节数组
 */
public class PrimitiveByteArray implements ArrayObject {

    protected byte[] array;

    public PrimitiveByteArray(int size) {
        array = new byte[size];
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (Byte) value;
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
