package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.ShortArray;

/**
 * 短整数数组提供者
 */
public class ShortArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new ShortArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Short.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
