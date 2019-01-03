package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;
import com.jd.laf.binding.util.Collections;

import java.util.Collection;

import static com.jd.laf.binding.Plugin.CONVERTER;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 集合换成集合的提供者
 */
public class Collection2CollectionSupplier implements ConverterSupplier {


    @Override
    public Converter getConverter(final ConversionType type) {
        if (Collection.class.isAssignableFrom(type.targetType) && Collection.class.isAssignableFrom(type.sourceType)) {
            return Collection2CollectionOperation.INSTANCE;
        }
        return null;
    }

    @Override
    public int order() {
        return COLLECTION_TO_COLLECTION_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static final class Collection2CollectionOperation implements Converter {

        protected static final Converter INSTANCE = new Collection2CollectionOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {

            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> inboxTargetComponentType = inbox(conversion.scope.getGenericType());
            if (inboxTargetComponentType == null) {
                return null;
            }

            Collection value = (Collection) conversion.source;
            int size = value.size();
            Collection result = Collections.create(conversion.targetType, size);
            if (result == null) {
                return null;
            }
            Object format = conversion.format;
            Class<?> inboxSourceComponentType, lastInboxSourceComponentType = null;
            Converter operation = null;
            Object obj;
            for (Object v : value) {
                if (v == null) {
                    result.add(null);
                } else {
                    inboxSourceComponentType = inbox(v.getClass());
                    operation = inboxSourceComponentType.equals(lastInboxSourceComponentType) ? operation :
                            CONVERTER.select(new ConversionType(inboxSourceComponentType, inboxTargetComponentType));
                    obj = operation.execute(new Conversion(inboxSourceComponentType, inboxTargetComponentType, v, format));
                    if (obj == null) {
                        //转换失败
                        return false;
                    }
                    lastInboxSourceComponentType = inboxSourceComponentType;
                    result.add(obj);
                }
            }
            return result;
        }
    }
}
