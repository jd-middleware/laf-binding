package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.IntegerArray;

/**
 * 整数数组提供者
 */
public class IntegerArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new IntegerArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Integer.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
