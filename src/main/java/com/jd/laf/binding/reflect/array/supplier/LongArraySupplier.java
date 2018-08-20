package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.LongArray;

/**
 * 长整数数组提供者
 */
public class LongArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new LongArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new LongArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Long.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
