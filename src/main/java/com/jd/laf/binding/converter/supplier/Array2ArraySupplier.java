package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.Converters;
import com.jd.laf.binding.reflect.array.ArrayObject;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;

import static com.jd.laf.binding.reflect.array.ArraySuppliers.getArraySupplier;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 数组转换成数组的提供者
 */
public class Array2ArraySupplier implements ConverterSupplier {

    @Override
    public Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        if (targetType.isArray() && sourceType.isArray()) {
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
    protected static final class Array2ArrayOperation implements Operation {

        protected static final Operation INSTANCE = new Array2ArrayOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            //获取数组元素类型
            Class<?> targetComponentType = conversion.getTargetType().getComponentType();
            Class<?> sourceComponentType = conversion.getSourceType().getComponentType();
            Class<?> inboxTargetComponentType = inbox(targetComponentType);
            Class<?> inboxSourceComponentType = inbox(sourceComponentType);
            if (inboxTargetComponentType == null || inboxSourceComponentType == null) {
                // 不支持转换
                return null;
            }
            if (inboxTargetComponentType.equals(inboxSourceComponentType)
                    || inboxTargetComponentType.isAssignableFrom(inboxSourceComponentType)) {
                //类型可以直接复制
                return conversion.getSource();
            }
            //获取转换器
            Operation componentOperation = Converters.getOperation(inboxSourceComponentType, inboxTargetComponentType);
            //用原始元素类型构建数组
            ArraySupplier srcArraySupplier = getArraySupplier(sourceComponentType);
            //构建数组
            ArraySupplier targetArraySupplier = getArraySupplier(targetComponentType);

            ArrayObject srcArray = srcArraySupplier.wrap(conversion.getSource());
            int size = srcArray.length();
            ArrayObject targetArray = targetArraySupplier.create(size);
            Object obj;
            Object element;
            Class<?> inboxSourceElementType, lastInboxSourceElementType = null;
            Operation op = null;
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
                                element, conversion.getFormat()));
                    } else {
                        //获取实际的元素类型
                        inboxSourceElementType = inbox(element.getClass());
                        //如果类型有变更，则根据实际类型获取转换器
                        op = inboxSourceElementType.equals(lastInboxSourceElementType) ? op :
                                Converters.getOperation(inboxSourceElementType, inboxTargetComponentType);
                        if (op == null) {
                            return null;
                        }
                        obj = op.execute(new Conversion(inboxSourceElementType, inboxTargetComponentType, element, conversion.getFormat()));
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
