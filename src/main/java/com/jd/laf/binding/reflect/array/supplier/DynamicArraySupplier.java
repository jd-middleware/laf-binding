package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.DoubleArray;

/**
 * 动态反射数组提供者
 */
public class DynamicArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new DoubleArray(size);
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
