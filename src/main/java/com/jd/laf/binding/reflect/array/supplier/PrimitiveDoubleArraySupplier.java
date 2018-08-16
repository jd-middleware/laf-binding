package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.PrimitiveDoubleArray;

/**
 * 双精度浮点数数组提供者
 */
public class PrimitiveDoubleArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new PrimitiveDoubleArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return double.class.equals(clazz);
    }

    @Override
    public int order() {
        return 0;
    }
}
