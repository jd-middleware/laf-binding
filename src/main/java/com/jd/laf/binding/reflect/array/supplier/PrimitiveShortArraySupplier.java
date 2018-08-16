package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.PrimitiveShortArray;

/**
 * 短整数数组提供者
 */
public class PrimitiveShortArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new PrimitiveShortArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return short.class.equals(clazz);
    }

    @Override
    public int order() {
        return 1;
    }
}
