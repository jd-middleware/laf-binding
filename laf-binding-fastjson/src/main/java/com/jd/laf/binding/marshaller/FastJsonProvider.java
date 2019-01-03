package com.jd.laf.binding.marshaller;

import com.alibaba.fastjson.JSON;

/**
 * FastJson提供者
 */
public class FastJsonProvider implements JsonProvider {
    @Override
    public Unmarshaller getUnmarshaller() {
        return FastJsonUnmarshaller.INSTANCE;
    }

    @Override
    public Marshaller getMarshaller() {
        return FastJsonMarshaller.INSTANCE;
    }

    static class FastJsonUnmarshaller implements Unmarshaller {
        static Unmarshaller INSTANCE = new FastJsonUnmarshaller();

        @Override
        public <T> T unmarshall(String value, Class<T> clazz, String format) throws Exception {
            return JSON.parseObject(value, clazz);
        }

        @Override
        public <T> T unmarshall(String value, Class<T> clazz) throws Exception {
            return unmarshall(value, clazz, null);
        }
    }

    static class FastJsonMarshaller implements Marshaller {
        static Marshaller INSTANCE = new FastJsonMarshaller();

        @Override
        public String marshall(Object target) throws Exception {
            return JSON.toJSONString(target);
        }
    }
}
