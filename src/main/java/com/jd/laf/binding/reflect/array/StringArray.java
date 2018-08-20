package com.jd.laf.binding.reflect.array;

/**
 * 字符串数组
 */
public class StringArray implements ArrayObject {

    protected String[] array;

    public StringArray(int size) {
        array = new String[size];
    }

    public StringArray(Object array) {
        this.array = (String[]) array;
    }

    @Override
    public void set(final int index, final Object value) {
        array[index] = (String) value;
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
