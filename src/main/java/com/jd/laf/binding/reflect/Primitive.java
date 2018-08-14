package com.jd.laf.binding.reflect;

/**
 * 私有对象工具类
 */
public abstract class Primitive {

    /**
     * 装箱
     *
     * @param clazz
     * @return
     */
    public static Class<?> inbox(final Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return clazz;
        } else if (int.class.equals(clazz)) {
            return Integer.class;
        } else if (double.class.equals(clazz)) {
            return Double.class;
        } else if (char.class.equals(clazz)) {
            return Character.class;
        } else if (boolean.class.equals(clazz)) {
            return Boolean.class;
        } else if (long.class.equals(clazz)) {
            return Long.class;
        } else if (float.class.equals(clazz)) {
            return Float.class;
        } else if (short.class.equals(clazz)) {
            return Short.class;
        } else if (byte.class.equals(clazz)) {
            return Byte.class;
        } else {
            return clazz;
        }
    }
}
