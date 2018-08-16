package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.PrimitiveBooleanArray;

/**
 * 布尔数组提供者
 */
public class PrimitiveBooleanArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new PrimitiveBooleanArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return boolean.class.equals(clazz);
    }

    @Override
    public int order() {
        return 1;
    }
}
