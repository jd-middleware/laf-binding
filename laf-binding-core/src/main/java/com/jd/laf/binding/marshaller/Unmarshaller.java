package com.jd.laf.binding.marshaller;

import com.jd.laf.binding.reflect.TypeReference;

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

    /**
     * 反序列化
     *
     * @param format
     * @return
     * @throws Exception
     */
    <T> T unmarshall(String value, TypeReference<T> reference, String format) throws Exception;

    /**
     * 反序列化
     *
     * @return
     * @throws Exception
     */
    <T> T unmarshall(String value, Class<T> clazz) throws Exception;

    /**
     * 反序列化
     *
     * @return
     * @throws Exception
     */
    <T> T unmarshall(String value, TypeReference<T> reference) throws Exception;

}
