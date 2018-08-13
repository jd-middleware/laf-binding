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
public @interface Value {

    /**
     * 键
     *
     * @return 键
     */
    String value() default "";

    /**
     * 数据转换参数
     *
     * @return
     */
    String format() default "";

}
