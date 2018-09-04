package com.jd.laf.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定上下文
 * Created by hexiaofeng on 15-7-20.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonValue {

    /**
     * 键
     *
     * @return 键
     */
    String value() default "";

    /**
     * 数据转换参数
     *
     * @return 转换参数
     */
    String format() default "";

    /**
     * 是否可以为空
     *
     * @return 为空标识
     */
    boolean nullable() default true;

}
