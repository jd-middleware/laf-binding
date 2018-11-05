package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;

import java.lang.reflect.Constructor;

import static com.jd.laf.binding.reflect.Constructors.getConstructor;

/**
 * 根据构造函数进行转换
 */
public class ConstructorSupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        // 判断是否有构造函数
        Constructor constructor = getConstructor(type.getTargetType(), type.getSourceType());
        return constructor == null ? null : new ConstructorOperation(constructor);
    }

    @Override
    public int order() {
        return CONSTRUCTOR_SUPPLIER_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static class ConstructorOperation implements Converter {

        protected final Constructor constructor;

        public ConstructorOperation(Constructor constructor) {
            this.constructor = constructor;
        }

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            return conversion == null ? null : constructor.newInstance(conversion.getSource());
        }
    }

}
