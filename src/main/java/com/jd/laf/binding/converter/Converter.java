package com.jd.laf.binding.converter;

/**
 * 类型转换
 */
public interface Converter {

    /**
     * 类型转换
     *
     * @param conversion 转换请求
     * @return
     */
    Object convert(Conversion conversion);

    /**
     * 是否支持该类型转换
     *
     * @param type
     * @return
     */
    boolean support(Class<?> type);

    /**
     * 支持的目标类型
     *
     * @return 字段类型
     */
    Class<?>[] types();

    /**
     * 转换请求
     */
    class Conversion {
        //原始对象
        protected final Object source;
        //目标类型
        protected final Class<?> type;
        //数据转换参数
        protected final Object format;

        public Conversion(Object source, Class<?> type) {
            this(source, type, null);
        }

        public Conversion(Object source, Class<?> type, Object format) {
            this.source = source;
            this.type = type;
            this.format = format;
        }

        public Object getSource() {
            return source;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getFormat() {
            return format;
        }
    }

}
