package com.jd.laf.binding.reflect;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.Scope;
import com.jd.laf.binding.converter.Scope.FieldScope;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static com.jd.laf.binding.Plugin.CONVERTER;
import static com.jd.laf.binding.Plugin.PROPERTY;
import static com.jd.laf.binding.reflect.Fields.getField;
import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 反射工具类
 */
public abstract class Reflect {

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
        PropertySupplier getter = PROPERTY.select(target.getClass());
        try {
            Object result = getter == null ? null : getter.get(target, name);
            if (result == null && Character.isJavaIdentifierStart(name.charAt(0))) {
                Field field = getField(target.getClass(), name);
                if (field != null) {
                    return factory.getAccessor(field).get(target);
                }
            }
            return result;
        } catch (ReflectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }

    /**
     * 绑定字段
     *
     * @param target 目标对象
     * @param field  字段
     * @param value  字段值
     * @return 是否成功
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value) throws ReflectionException {
        if (field == null || target == null) {
            return false;
        }
        return set(target, field, value, null, ReflectAccessorFactory.getInstance().getAccessor(field));
    }

    /**
     * 绑定字段
     *
     * @param target  目标对象
     * @param field   字段
     * @param value   字段值
     * @param format  格式化
     * @param factory 字段访问工厂
     * @return 是否成功
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value, final Object format,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || target == null || factory == null) {
            return false;
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
     * @return 是否成功
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || target == null || factory == null) {
            return false;
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
     * @return 是否成功
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value, final Object format,
                              final FieldAccessor accessor) throws ReflectionException {
        if (field == null || target == null || accessor == null || Modifier.isFinal(field.getModifiers())) {
            return false;
        }
        try {
            Class<?> type = field.getType();
            if (value == null) {
                //值为空
                if (type.isPrimitive()) {
                    //基本类型不能为空
                    return false;
                }
                accessor.set(target, null);
                return true;
            }
            Class<?> inboxTargetType = inbox(field.getType());
            Class<?> inboxSourceType = inbox(value.getClass());
            FieldScope scope = new FieldScope(field);
            //获取转换器
            Converter operation = CONVERTER.select(new ConversionType(inboxSourceType, inboxTargetType, scope));
            if (operation != null) {
                //拿到转换器
                Object obj = operation.execute(new Conversion(inboxSourceType, inboxTargetType, value, format, scope));
                if (obj != null) {
                    //转换成功
                    accessor.set(target, obj);
                    return true;
                }
            }
            return false;
        } catch (ReflectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }

    /**
     * 是否支持类型转换
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean support(final Class<?> source, final Class<?> target) {
        if (source == null || target == null) {
            return false;
        }
        //获取转换器
        return CONVERTER.select(new ConversionType(inbox(source), inbox(target))) != null;
    }

    /**
     * 在作用域上是否支持类型转换，支持作用域上的转换注解
     *
     * @param source 源类型
     * @param target 目标类型
     * @param scope  作用域
     * @return
     */
    public static boolean support(final Class<?> source, final Class<?> target, final Scope scope) {
        if (source == null || target == null) {
            return false;
        }
        return CONVERTER.select(new ConversionType(inbox(source), inbox(target), scope)) != null;
    }

}
