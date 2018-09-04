package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.Converters;
import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.ArraySuppliers;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;

import java.util.List;

import static com.jd.laf.binding.util.Primitive.inbox;
import static com.jd.laf.binding.util.Strings.split;

/**
 * 字符串转换成数组的提供者
 */
public class String2ArraySupplier implements ConverterSupplier {

    @Override
    public Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        if (targetType.isArray() && CharSequence.class.isAssignableFrom(sourceType)) {
            return String2ArrayOperation.INSTANCE;
        }
        return null;
    }

    @Override
    public int order() {
        return STRING_TO_ARRAY_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static final class String2ArrayOperation implements Operation {

        protected static final Operation INSTANCE = new String2ArrayOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            Class<?> targetElementType = conversion.getTargetType().getComponentType();
            Class<?> inboxElementType = inbox(targetElementType);
            Operation operation = Converters.getPlugin(String.class, inboxElementType);
            if (operation == null) {
                return null;
            }
            Object format = conversion.getFormat();
            //分割字符串
            List<String> parts = split(conversion.getSource().toString(), format == null ? null : format.toString());
            //构建数组
            ArraySupplier arraySupplier = ArraySuppliers.getArraySupplier(targetElementType);
            ArrayObject array = arraySupplier.create(parts.size());
            int pos = 0;
            Object obj;
            //遍历分割后的字符串进行转换
            for (String part : parts) {
                if (part != null) {
                    part = part.trim();
                }
                if (part == null || part.isEmpty()) {
                    //空字符串
                    if (targetElementType.isPrimitive()) {
                        //基本类型，不支持null
                        return null;
                    } else {
                        array.set(pos++, null);
                    }
                } else {
                    obj = operation.execute(new Conversion(String.class, inboxElementType, part));
                    if (obj == null) {
                        return null;
                    }
                    array.set(pos++, obj);
                }
            }
            return array.getArray();
        }
    }
}
