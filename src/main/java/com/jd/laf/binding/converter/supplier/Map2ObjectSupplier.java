package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.reflect.Fields;
import com.jd.laf.binding.reflect.Reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import static com.jd.laf.binding.reflect.Constructors.getDefaultConstructor;

public class Map2ObjectSupplier implements ConverterSupplier {

    @Override
    public Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        if (!Map.class.isAssignableFrom(sourceType)
                || Object.class.equals(targetType)
                || Map.class.isAssignableFrom(targetType)
                || Collection.class.isAssignableFrom(targetType)
                || targetType.isArray()
                || targetType.isInterface()
                || targetType.isPrimitive()
                || targetType.isEnum()
                || Modifier.isAbstract(targetType.getModifiers())) {
            return null;
        }
        return Map2ObjectOperation.INSTANCE;
    }

    @Override
    public int order() {
        return MAP_TO_OBJECT_ORDER;
    }

    /**
     * MAP转换成对象操作
     */
    protected static class Map2ObjectOperation implements Operation {

        protected static final Operation INSTANCE = new Map2ObjectOperation();

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            Class<?> targetType = conversion.getTargetType();
            Constructor constructor = getDefaultConstructor(targetType);
            if (constructor == null) {
                return null;
            }
            Object obj = constructor.newInstance();
            Object key;
            Field field;
            Map<String, Field> fields = Fields.getFields(targetType);
            if (!fields.isEmpty()) {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) conversion.getSource()).entrySet()) {
                    key = entry.getKey();
                    if (key != null && (key instanceof CharSequence)) {
                        field = fields.get(key.toString());
                        if (field != null) {
                            Reflect.set(obj, field, entry.getValue());
                        }
                    }
                }
            }

            return obj;
        }
    }
}
