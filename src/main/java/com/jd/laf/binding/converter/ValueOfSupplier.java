package com.jd.laf.binding.converter;

/**
 * 根据静态的valueOf方法进行转换
 */
public class ValueOfSupplier extends MethodSupplier {

    public static final String VALUE_OF = "valueOf";

    public ValueOfSupplier() {
        super(VALUE_OF);
    }
}
