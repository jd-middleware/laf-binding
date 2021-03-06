package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.DoubleArray;

/**
 * 双精度浮点数数组提供者
 */
public class DoubleArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new DoubleArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new DoubleArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Double.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
