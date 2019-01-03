package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.ByteArray;

/**
 * 字节数组提供者
 */
public class ByteArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(final int size) {
        return new ByteArray(size);
    }

    @Override
    public ArrayObject wrap(final Object array) {
        return new ByteArray(array);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return Byte.class.equals(clazz);
    }

    @Override
    public int order() {
        return 2;
    }
}
