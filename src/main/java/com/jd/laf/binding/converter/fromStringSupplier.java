package com.jd.laf.binding.converter;

/**
 * 根据静态的fromString方法进行转换
 */
public class fromStringSupplier extends MethodSupplier {

    public static final String FROM_STRING = "fromString";

    public fromStringSupplier() {
        super(FROM_STRING);
    }
}
