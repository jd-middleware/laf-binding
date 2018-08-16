package com.jd.laf.binding.reflect.array;

/**
 * 字节数组
 */
public class ByteArray implements ArrayObject {

    protected Byte[] array;

    public ByteArray(int size) {
        array = new Byte[size];
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
