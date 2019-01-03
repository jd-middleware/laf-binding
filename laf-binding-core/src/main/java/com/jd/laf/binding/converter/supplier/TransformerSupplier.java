package com.jd.laf.binding.converter.supplier;


import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;

import static com.jd.laf.binding.Plugin.TRANSFORMER;


/**
 * 注解转换器提供者
 * Created by hexiaofeng on 16-8-29.
 */
public class TransformerSupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        if (type == null || type.scope == null) {
            return null;
        }

        return TRANSFORMER.select(type);
    }

    @Override
    public int order() {
        return TRANSFORMER_SUPPLIER_ORDER;
    }

}
