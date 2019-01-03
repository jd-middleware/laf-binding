package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.PrimitiveFloatArray;

/**
 * 单精度浮点数数组提供者
 */
public class PrimitiveFloatArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new PrimitiveFloatArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new PrimitiveFloatArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return float.class.equals(clazz);
    }

    @Override
    public int order() {
        return 1;
    }
}
