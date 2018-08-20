package com.jd.laf.binding.reflect.array.supplier;

import com.jd.laf.binding.reflect.array.ArrayObject;

/**
 * 数组提供者
 */
public interface ArraySupplier {

    /**
     * 创建数组
     *
     * @param size 长度
     * @return 数组对象
     */
    ArrayObject create(int size);

    /**
     * 把数组包装成数组对象
     *
     * @param array 数组
     * @return 数组对象
     */
    ArrayObject wrap(Object array);

    /**
     * 是否支持数组元素类型
     *
     * @param clazz 数组元素类型
     * @return
     */
    boolean support(Class<?> clazz);

    /**
     * 优先级，数值越低优先级越高
     *
     * @return
     */
    int order();
}
