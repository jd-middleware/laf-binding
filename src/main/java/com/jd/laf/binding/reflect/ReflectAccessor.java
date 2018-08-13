package com.jd.laf.binding.reflect;

import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射字段访问器
 */
public class ReflectAccessor implements FieldAccessor {
    // 字段
    private Field field;
    // 获取方法
    private Method getter;
    // 设置方法
    private Method setter;

    public ReflectAccessor(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("getField can not be null.");
        }
        this.field = field;
        Class<?> clazz = field.getDeclaringClass();
        if (clazz != null) {
            String name = field.getName();
            char[] data = name.toCharArray();
            data[0] = Character.toUpperCase(data[0]);
            name = new String(data);
            String getName = "get" + name;
            String getBoolName = (field.getType() == boolean.class || field.getType() == Boolean.class) ? "is" + name : null;
            String setName = "set" + name;
            Class<?>[] types;
            int count = 0;
            // 获取GETTER方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (Modifier.isPublic(method.getModifiers())) {
                    // 方法的名称会放入JVM的常量池里面
                    name = method.getName();
                    if (name.equals(getName) || (getBoolName != null && name.equals(getBoolName))) {
                        types = method.getParameterTypes();
                        if (types == null || types.length == 0) {
                            getter = method;
                            count++;
                        }
                    } else if (name.equals(setName)) {
                        types = method.getParameterTypes();
                        if (types != null && types.length == 1 && types[0] == field.getType()) {
                            setter = method;
                            count++;
                        }
                    }
                    if (count == 2) {
                        break;
                    }

                }
            }
        }
    }

    @Override
    public Object get(final Object target) throws ReflectionException {
        try {
            if (target == null) {
                return null;
            } else if (getter != null) {
                return getter.invoke(target);
            } else if (field.isAccessible()) {
                return field.get(target);
            } else {
                field.setAccessible(true);
                try {
                    return field.get(target);
                } finally {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }

    }

    @Override
    public void set(final Object target, final Object value) throws ReflectionException {
        try {
            if (target == null) {
                return;
            } else if (setter != null) {
                setter.invoke(target, value);
            } else if (field.isAccessible()) {
                field.set(target, value);
            } else {
                field.setAccessible(true);
                try {
                    field.set(target, value);
                } finally {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }

}
