package com.jd.laf.binding.marshaller;

/**
 * XML提供者
 */
public interface XmlProvider {

    /**
     * 创建反序列化对象
     *
     * @return 反序列化对象
     */
    Unmarshaller getUnmarshaller();

}
