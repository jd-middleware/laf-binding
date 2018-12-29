package com.jd.laf.binding.converter;

import java.lang.reflect.Method;

/**
 * 转换操作
 */
public interface Converter {

    /**
     * 执行
     *
     * @param conversion 转换对象
     * @return 转换后的对象
     * @throws Exception
     */
    Object execute(Conversion conversion) throws Exception;

    //不做转换
    Converter NONE = new Converter() {
        @Override
        public Object execute(Conversion conversion) {
            return conversion.source;
        }
    };

    /**
     * 构造函数操作
     */
    class MethodConverter implements Converter {

        protected final Method method;

        public MethodConverter(Method method) {
            this.method = method;
        }

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            return conversion == null ? null : method.invoke(null, conversion.source);
        }
    }
}
