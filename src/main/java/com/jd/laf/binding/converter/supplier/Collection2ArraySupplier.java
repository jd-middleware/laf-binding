package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.Converters;
import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;

import java.util.Collection;

import static com.jd.laf.binding.reflect.array.ArraySuppliers.getArraySupplier;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 集合转换成数组的提供者
 */
public class Collection2ArraySupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        if (type.getTargetType().isArray() && Collection.class.isAssignableFrom(type.getSourceType())) {
            return Collection2ArrayOperation.INSTANCE;
        }
        return null;
    }

    @Override
    public int order() {
        return COLLECTION_TO_ARRAY_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static final class Collection2ArrayOperation implements Converter {

        protected static final Converter INSTANCE = new Collection2ArrayOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            Class<?> targetComponentType = conversion.getTargetType().getComponentType();
            Class<?> inboxTargetComponentType = inbox(targetComponentType);
            if (inboxTargetComponentType == null) {
                return null;
            }
            Class<?> inboxSourceComponentType, lastInboxSourceComponentType = null;
            final Collection<?> value = (Collection<?>) conversion.getSource();
            final Object format = conversion.getFormat();
            //集合大小
            int size = value.size();
            ArraySupplier arraySupplier = getArraySupplier(targetComponentType);
            ArrayObject array = arraySupplier.create(size);
            Converter operation = null;
            Object obj;
            int count = 0;
            for (Object v : value) {
                if (v == null) {
                    if (targetComponentType.isPrimitive()) {
                        return false;
                    }
                    array.set(count++, null);
                } else {
                    inboxSourceComponentType = inbox(v.getClass());
                    operation = inboxSourceComponentType.equals(lastInboxSourceComponentType) ? operation :
                            Converters.getPlugin(inboxSourceComponentType, inboxTargetComponentType);
                    obj = operation.execute(new Conversion(inboxSourceComponentType, inboxTargetComponentType, v, format));
                    if (obj == null) {
                        return false;
                    }
                    array.set(count++, obj);
                    lastInboxSourceComponentType = inboxSourceComponentType;
                }
            }
            return array.getArray();
        }
    }
}
