package com.jd.laf.binding.reflect.array;

/**
 * 数组对象，动态创建数组耗费时间，增对基本类型的数组进行包装
 */
public interface ArrayObject {

    /**
     * 设置值
     *
     * @param index
     * @param value
     */
    void set(int index, Object value);

    /**
     * 获取值
     *
     * @param index
     * @return
     */
    Object get(int index);

    /**
     * 数组长度
     *
     * @return
     */
    int length();

    /**
     * 获取数组对象
     *
     * @return
     */
    Object getArray();
}
