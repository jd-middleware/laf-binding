package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.DoubleArray;
import com.jd.laf.binding.reflect.array.DynamicArray;

/**
 * 动态反射数组提供者
 */
public class DynamicArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new DynamicArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new DynamicArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return true;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
