package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.FloatArray;

/**
 * 单精度浮点数数组提供者
 */
public class FloatArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new FloatArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new FloatArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Float.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
