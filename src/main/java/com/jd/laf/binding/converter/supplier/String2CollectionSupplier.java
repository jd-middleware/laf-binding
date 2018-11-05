package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.Converters;
import com.jd.laf.binding.util.Collections;

import java.util.Collection;
import java.util.List;

import static com.jd.laf.binding.reflect.Generics.getGenericType;
import static com.jd.laf.binding.util.Primitive.inbox;
import static com.jd.laf.binding.util.Strings.split;

/**
 * 字符串转换成集合的提供者
 */
public class String2CollectionSupplier implements ConverterSupplier {


    @Override
    public Converter getConverter(final ConversionType type) {
        if (Collection.class.isAssignableFrom(type.getTargetType()) && CharSequence.class.isAssignableFrom(type.getSourceType())) {
            return String2CollectionOperation.INSTANCE;
        }
        return null;
    }

    @Override
    public int order() {
        return STRING_TO_COLLECTION_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static final class String2CollectionOperation implements Converter {

        protected static final Converter INSTANCE = new String2CollectionOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            Class<?> inboxTargetComponentType = inbox(conversion.getScope().getGenericType());
            if (inboxTargetComponentType == null) {
                return null;
            }
            Converter operation = Converters.getPlugin(String.class, inboxTargetComponentType);
            if (operation == null) {
                return null;
            }
            Object format = conversion.getFormat();
            //分割字符串
            List<String> parts = split(conversion.getSource().toString(), format == null ? null : format.toString());
            Collection result = Collections.create(conversion.getTargetType(), parts.size());
            if (result == null) {
                //不支持的集合类型
                return null;
            }
            //遍历分割后的字符串进行转换
            Object obj;
            for (String part : parts) {
                if (part != null) {
                    part = part.trim();
                }
                if (part == null || part.isEmpty()) {
                    //空字符串，转换成null对象
                    result.add(null);
                } else {
                    obj = operation.execute(new Conversion(String.class, inboxTargetComponentType, part));
                    if (obj == null) {
                        return null;
                    }
                    result.add(obj);
                }
            }
            return result;
        }
    }
}
