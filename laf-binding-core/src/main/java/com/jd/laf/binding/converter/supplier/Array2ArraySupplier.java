package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;
import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;

import static com.jd.laf.binding.Plugin.ARRAY;
import static com.jd.laf.binding.Plugin.CONVERTER;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 数组转换成数组的提供者
 */
public class Array2ArraySupplier implements ConverterSupplier {

    @Override
    public Converter getConverter(final ConversionType type) {
        if (type.targetType.isArray() && type.sourceType.isArray()) {
            return Array2ArrayOperation.INSTANCE;
        }
        return null;
    }

    @Override
    public int order() {
        return ARRAY_TO_ARRAY_ORDER;
    }

    /**
     * 构造函数操作
     */
    protected static final class Array2ArrayOperation implements Converter {

        protected static final Converter INSTANCE = new Array2ArrayOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            //获取数组元素类型
            Class<?> targetComponentType = conversion.targetType.getComponentType();
            Class<?> sourceComponentType = conversion.sourceType.getComponentType();
            Class<?> inboxTargetComponentType = inbox(targetComponentType);
            Class<?> inboxSourceComponentType = inbox(sourceComponentType);
            if (inboxTargetComponentType == null || inboxSourceComponentType == null) {
                // 不支持转换
                return null;
            }
            if (inboxTargetComponentType.equals(inboxSourceComponentType)
                    || inboxTargetComponentType.isAssignableFrom(inboxSourceComponentType)) {
                //类型可以直接复制
                return conversion.source;
            }
            //获取转换器
            Converter componentOperation = CONVERTER.select(new ConversionType(inboxSourceComponentType, inboxTargetComponentType));
            //用原始元素类型构建数组
            ArraySupplier srcArraySupplier = ARRAY.select(sourceComponentType);
            //构建数组
            ArraySupplier targetArraySupplier = ARRAY.select(targetComponentType);

            ArrayObject srcArray = srcArraySupplier.wrap(conversion.source);
            int size = srcArray.length();
            ArrayObject targetArray = targetArraySupplier.create(size);
            Object obj;
            Object element;
            Class<?> inboxSourceElementType, lastInboxSourceElementType = null;
            Converter op = null;
            //遍历数组
            for (int i = 0; i < size; i++) {
                element = srcArray.get(i);
                if (element == null) {
                    if (targetComponentType.isPrimitive()) {
                        //基本元素不支持null
                        return null;
                    } else {
                        targetArray.set(i, null);
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
                    targetArray.set(i, obj);
                }
            }
            return targetArray.getArray();
        }
    }
}
