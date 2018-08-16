package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.StringArray;

/**
 * 字符串数组提供者
 */
public class StringArraySupplier implements ArraySupplier {

    @Override
    public ArrayObject create(int size) {
        return new StringArray(size);
    }

    @Override
    public boolean support(final Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public int order() {
        return 0;
    }
}
