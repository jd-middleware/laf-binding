package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.PrimitiveByteArray;

/**
 * 字节数组提供者
 */
public class PrimitiveByteArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new PrimitiveByteArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return byte.class.equals(clazz);
    }

    @Override
    public int order() {
        return 1;
    }
}
