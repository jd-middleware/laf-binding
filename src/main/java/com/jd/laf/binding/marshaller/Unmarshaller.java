package com.jd.laf.binding.marshaller;

/**
 * 反序列化
 */
public interface Unmarshaller {

    /**
     * 反序列化
     *
     * @param format
     * @return
     * @throws Exception
     */
    <T> T unmarshall(String value, Class<T> clazz, String format) throws Exception;

}
