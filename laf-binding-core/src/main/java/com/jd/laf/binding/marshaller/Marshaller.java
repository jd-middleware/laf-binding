package com.jd.laf.binding.marshaller;

/**
 * 序列化
 */
public interface Marshaller {

    /**
     * 序列化
     *
     * @param target
     * @return
     * @throws Exception
     */
    String marshall(Object target) throws Exception;

}
