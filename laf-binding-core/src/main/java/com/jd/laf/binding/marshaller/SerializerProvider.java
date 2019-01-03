package com.jd.laf.binding.marshaller;

/**
 * 序列化提供者
 */
public interface SerializerProvider {

    /**
     * 创建反序列化对象
     *
     * @return 反序列化对象
     */
    Unmarshaller getUnmarshaller();

    /**
     * 创建序列化对象
     *
     * @return 序列化对象
     */
    Marshaller getMarshaller();

}
