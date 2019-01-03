package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.BooleanArray;

/**
 * 布尔数组提供者
 */
public class BooleanArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new BooleanArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new BooleanArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Boolean.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
