package com.jd.laf.binding.marshaller;

import com.alibaba.fastjson.JSON;
import com.jd.laf.extension.Extension;

/**
 * FastJson提供者
 */
@Extension(value = "fastjson", provider = "alibaba", order = 100)
public class FastJsonProvider implements JsonProvider {
    @Override
    public Unmarshaller getUnmarshaller() {
        return FastJsonUnmarshaller.INSTANCE;
    }

    @Override
    public Marshaller getMarshaller() {
        return FastJsonMarshaller.INSTANCE;
    }

    /**
     * FastJson实现
     */
    static class FastJsonUnmarshaller implements Unmarshaller {
        static Unmarshaller INSTANCE = new FastJsonUnmarshaller();

        @Override
        public <T> T unmarshall(final String value, final Class<T> clazz, final String format) throws Exception {
            return JSON.parseObject(value, clazz);
        }

        @Override
        public <T> T unmarshall(final String value, final Class<T> clazz) throws Exception {
            return JSON.parseObject(value, clazz);
        }

        @Override
        public <T> T unmarshall(final String value, final TypeReference<T> reference, final String format) throws Exception {
            return JSON.parseObject(value, reference.getType());
        }

        @Override
        public <T> T unmarshall(final String value, final TypeReference<T> reference) throws Exception {
            return JSON.parseObject(value, reference.getType());
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
