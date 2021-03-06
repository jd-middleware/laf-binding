package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;
import com.jd.laf.binding.reflect.GenericMeta;
import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;
import com.jd.laf.binding.util.Collections;

import java.util.Collection;

import static com.jd.laf.binding.Plugin.ARRAY;
import static com.jd.laf.binding.Plugin.CONVERTER;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 数组转换成数组的提供者
 */
public class Array2CollectionSupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        if (Collection.class.isAssignableFrom(type.targetType) && type.sourceType.isArray()) {
            return Array2CollectionOperation.INSTANCE;
        }
        return null;
    }

    @Override
    public int order() {
        return ARRAY_TO_COLLECTION_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static final class Array2CollectionOperation implements Converter {

        protected static final Converter INSTANCE = new Array2CollectionOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            //获取字段泛型
            Class<?> inboxTargetComponentType = inbox(conversion.scope.getGenericType());
            if (inboxTargetComponentType == null) {
                return null;
            }
            Class<?> sourceComponentType = conversion.sourceType.getComponentType();
            Class<?> inboxSourceComponentType = inbox(sourceComponentType);
            //构建数组
            ArraySupplier srcArraySupplier = ARRAY.select(sourceComponentType);
            ArrayObject srcArray = srcArraySupplier.wrap(conversion.source);
            //数组大小
            int size = srcArray.length();
            Collection result = Collections.create(conversion.targetType, size);
            if (result == null) {
                //不支持的集合类型
                return null;
            }
            Converter componentOperation = CONVERTER.select(new ConversionType(inboxSourceComponentType, inboxTargetComponentType));
            Object obj;
            Object element;
            Converter op = null;
            Class<?> inboxSourceElementType, lastInboxSourceElementType = null;
            //遍历数组
            for (int i = 0; i < size; i++) {
                //递归转换元素
                element = srcArray.get(i);
                if (element == null) {
                    if (inboxTargetComponentType.isPrimitive()) {
                        //基本元素不支持null
                        return null;
                    } else {
                        result.add(null);
                    }
                } else {
                    if (componentOperation != null) {
                        //集合元素类型有转换器
                        obj = componentOperation.execute(new Conversion(inboxSourceComponentType, inboxTargetComponentType,
                                element, conversion.format));
                    } else {
                        //获取实际的元素类型
                        inboxSourceElementType = inbox(element.getClass());
                        //如果类型有变更，则根据实际类型获取转换器
                        op = inboxSourceElementType.equals(lastInboxSourceElementType) ? op :
                                CONVERTER.select(new ConversionType(inboxSourceElementType, inboxTargetComponentType));
                        if (op == null) {
                            return null;
                        }
                        obj = op.execute(new Conversion(inboxSourceElementType, inboxTargetComponentType, element, conversion.format));
                        lastInboxSourceElementType = inboxSourceElementType;
                    }
                    if (obj == null) {
                        //转换失败
                        return null;
                    }
                    result.add(obj);
                }

            }
            return result;
        }

    }
}
