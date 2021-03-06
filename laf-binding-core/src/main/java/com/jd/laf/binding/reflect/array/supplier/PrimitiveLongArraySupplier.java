package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.PrimitiveLongArray;

/**
 * 长整数数组提供者
 */
public class PrimitiveLongArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new PrimitiveLongArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new PrimitiveLongArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return long.class.equals(clazz);
    }

    @Override
    public int order() {
        return 0;
    }
}
