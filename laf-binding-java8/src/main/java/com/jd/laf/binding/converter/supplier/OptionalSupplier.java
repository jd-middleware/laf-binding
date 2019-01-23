package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;

import java.util.Optional;

import static com.jd.laf.binding.Plugin.CONVERTER;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 选项提供者
 */
public class OptionalSupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        if (!Optional.class.isAssignableFrom(type.targetType)) {
            return null;
        }
        return OptionOperation.INSTANCE;
    }

    @Override
    public int order() {
        return SIMPLE_SUPPLIER_ORDER;
    }

    /**
     * Option转换操作
     */
    protected static class OptionOperation implements Converter {

        public static final OptionOperation INSTANCE = new OptionOperation();

        @Override
        public Optional execute(final Conversion conversion) throws Exception {
            Object obj = null;
            //获取Option泛型类型作为目标转换类型
            Class clazz = conversion.scope.getGenericType();
            if (clazz != null) {
                //对目标转换类型进行封箱
                Class<?> inboxTargetType = inbox(clazz);
                //获取转换器
                Converter operation = CONVERTER.select(new ConversionType(conversion.sourceType, inboxTargetType, conversion.scope));
                if (operation != null) {
                    //拿到转换器
                    obj = operation.execute(new Conversion(conversion.sourceType, inboxTargetType, conversion.source, conversion.format, conversion.scope));
                }
            }
            return Optional.ofNullable(obj);
        }
    }

}