package com.jd.laf.binding.marshaller;

/**
 * JSON提供者
 */
public interface JsonProvider {

    /**
     * 创建反序列化对象
     *
     * @return 反序列化对象
     */
    Unmarshaller getUnmarshaller();

}
