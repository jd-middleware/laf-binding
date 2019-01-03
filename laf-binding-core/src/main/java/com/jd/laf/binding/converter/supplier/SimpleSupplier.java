package com.jd.laf.binding.converter.supplier;


import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;

import static com.jd.laf.binding.Plugin.SIMPLE;

/**
 * 简单类型转换器提供者
 * Created by hexiaofeng on 16-8-29.
 */
public class SimpleSupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        //去掉Scope作用域
        return SIMPLE.select(new ConversionType(type.sourceType, type.targetType));
    }

    @Override
    public int order() {
        return SIMPLE_SUPPLIER_ORDER;
    }

}
