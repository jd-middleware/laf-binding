package com.jd.laf.binding.reflect;

import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConverterSupplier;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.converter.Converters.getOperation;
import static com.jd.laf.binding.reflect.Primitive.inbox;
import static com.jd.laf.binding.reflect.PropertyGetters.getPropertyGetter;

/**
 * 反射工具类
 */
public abstract class Reflect {

    //类的字段名和字段映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<String, Option<Field>>> fields =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<String, Option<Field>>>();

    /**
     * 获取类的字段
     *
     * @param clazz 类
     * @param name  属性名
     * @return 字段
     * @throws ReflectionException
     */
    public static Field getField(final Class<?> clazz, final String name) throws ReflectionException {
        if (clazz == null || clazz.isPrimitive() || name == null || name.isEmpty()) {
            return null;
        }
        ConcurrentMap<String, Option<Field>> options = fields.get(clazz);
        if (options == null) {
            options = new ConcurrentHashMap<String, Option<Field>>();
            ConcurrentMap<String, Option<Field>> exist = fields.putIfAbsent(clazz, options);
            if (exist != null) {
                options = exist;
            }
        }
        Option<Field> option = options.get(name);
        if (option == null) {
            Field field = null;
            SuperClassIterator iterator = new SuperClassIterator(clazz);
            while (iterator.hasNext()) {
                try {
                    field = iterator.next().getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException e) {
                } catch (SecurityException e) {
                    throw new ReflectionException(e.getMessage(), e);
                }
            }
            option = new Option<Field>(field);
            Option<Field> exist = options.putIfAbsent(name, option);
            if (exist != null) {
                option = exist;
            }

        }
        return option.get();
    }

    /**
     * 解析表达式，获取属性值
     *
     * @param target     目标对象
     * @param expression 表达式
     * @param factory    字段访问工厂
     * @return
     * @throws ReflectionException
     */
    public static Object evaluate(final Object target, final String expression, final FieldAccessorFactory factory) throws ReflectionException {
        if (target == null || expression == null) {
            return null;
        }
        String name = expression;
        if (name.length() > 3) {
            char first = name.charAt(0);
            char second = name.charAt(1);
            char end = name.charAt(name.length() - 1);
            if ((first == '$' || first == '#') && second == '{' && end == '}') {
                name = name.substring(2, name.length() - 1);
            }
        }
        Object value = get(target, name, factory);
        if (value != null) {
            return value;
        }
        //判断嵌套属性
        int pos = name.indexOf('.');
        if (pos <= 0) {
            return null;
        }
        //处理表达式
        int len = name.length();
        int start = 0;
        while (start < len) {
            value = get(start == 0 ? target : value, name.substring(start, pos < 0 ? len : pos), factory);
            if (value == null) {
                return null;
            } else {
                start = pos < 0 ? len : pos + 1;
                pos = name.indexOf('.', start);
            }

        }
        return value;
    }

    /**
     * 获取属性值
     *
     * @param target  目标对象
     * @param name    表达式
     * @param factory 字段访问工厂
     * @return
     * @throws ReflectionException
     */
    public static Object get(final Object target, final String name, final FieldAccessorFactory factory) throws ReflectionException {
        if (target == null || name == null || name.isEmpty() || factory == null) {
            return null;
        }
        PropertyGetter getter = getPropertyGetter(target.getClass());
        Object result = getter == null ? null : getter.get(target, name);
        if (result == null && Character.isJavaIdentifierStart(name.charAt(0))) {
            Field field = getField(target.getClass(), name);
            if (field != null) {
                return factory.getAccessor(field).get(target);
            }
        }
        return result;
    }


    /**
     * 绑定字段
     *
     * @param target  目标对象
     * @param field   字段
     * @param value   字段值
     * @param format  格式化
     * @param factory 字段访问工厂
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value, final Object format,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || value == null || target == null || factory == null) {
            return true;
        }
        return set(target, field, value, format, factory.getAccessor(field));
    }

    /**
     * 绑定字段
     *
     * @param target  目标对象
     * @param field   字段
     * @param value   字段值
     * @param factory 字段访问工厂
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || value == null || target == null || factory == null) {
            return true;
        }
        return set(target, field, value, null, factory.getAccessor(field));
    }

    /**
     * 绑定字段
     *
     * @param target   目标对象
     * @param field    字段
     * @param value    字段值
     * @param accessor 字段访问对象
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value,
                              final FieldAccessor accessor) throws ReflectionException {
        return set(target, field, value, null, accessor);
    }

    /**
     * 绑定字段
     *
     * @param target   目标对象
     * @param field    字段
     * @param value    字段值
     * @param format   格式化信息
     * @param accessor 字段访问对象
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value, final Object format,
                              final FieldAccessor accessor) throws ReflectionException {
        if (field == null || value == null || target == null || accessor == null) {
            return true;
        }
        Object v = value;
        Class<?> targetType = inbox(field.getType());
        Class<?> sourceType = inbox(v.getClass());
        if (targetType == sourceType || targetType.isAssignableFrom(sourceType)) {
            //可以直接赋值，进行校验
            accessor.set(target, v);
            return true;
        } else {
            ConverterSupplier.Operation operation = getOperation(sourceType, targetType);
            if (operation != null) {
                try {
                    v = operation.execute(new Conversion(sourceType, targetType, v, format));
                    if (v != null) {
                        accessor.set(target, v);
                    }
                } catch (ReflectionException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ReflectionException(e.getMessage(), e);
                }
            }
        }
        return false;
    }

}
