package com.jd.laf.binding.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 来源于fastjson，便于插件实现
 *
 * @param <T>
 * @see com.alibaba.fastjson.TypeReference
 */
public abstract class TypeReference<T> {
    protected static ConcurrentMap<Type, Type> TYPE = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);
    public final static Type LIST_STRING = new TypeReference<List<String>>() {
    }.getType();

    protected final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        this.type = TYPE.putIfAbsent(type, type);
    }

    protected TypeReference(final Type rawType, Type[] argTypes) {
        Type key = new ParameterizedTypeImpl(argTypes, this.getClass(), rawType);
        this.type = TYPE.putIfAbsent(key, key);
    }

    protected TypeReference(final Type... actualTypeArguments) {
        Class<?> thisClass = this.getClass();
        Type superClass = thisClass.getGenericSuperclass();

        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) superClass).getActualTypeArguments()[0];

        Type key = handle(thisClass, argType, actualTypeArguments, 0);
        this.type = TYPE.putIfAbsent(key, key);
    }


    /**
     * 处理参数类型
     *
     * @param thisClass
     * @param type
     * @param actualTypeArguments
     * @param actualIndex
     * @return
     */
    protected Type handle(final Class<?> thisClass, final ParameterizedType type, final Type[] actualTypeArguments, int actualIndex) {
        Type[] types = type.getActualTypeArguments();

        for (int i = 0; i < types.length; ++i) {
            //设置成注入的类型
            if (types[i] instanceof TypeVariable && actualIndex < actualTypeArguments.length) {
                types[i] = actualTypeArguments[actualIndex++];
            }
            // fix for openjdk and android env
            if (types[i] instanceof GenericArrayType) {
                types[i] = checkPrimitiveArray((GenericArrayType) types[i]);
            }
            // 嵌套
            if (types[i] instanceof ParameterizedType) {
                types[i] = handle(thisClass, (ParameterizedType) types[i], actualTypeArguments, actualIndex);
            }
        }

        return new ParameterizedTypeImpl(types, thisClass, type.getRawType());
    }

    /**
     * 基本数组类型
     *
     * @param genericArrayType
     * @return
     */
    protected Type checkPrimitiveArray(final GenericArrayType genericArrayType) {
        Type clz = genericArrayType;

        //处理嵌套的泛型数组类型，得到数组的元素类型
        Type genericComponentType = genericArrayType.getGenericComponentType();
        String prefix = "[";
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType)
                    .getGenericComponentType();
            prefix += prefix;
        }

        if (genericComponentType instanceof Class<?>) {
            Class<?> ck = (Class<?>) genericComponentType;
            if (ck.isPrimitive()) {
                try {
                    if (ck == boolean.class) {
                        clz = Class.forName(prefix + "Z");
                    } else if (ck == char.class) {
                        clz = Class.forName(prefix + "C");
                    } else if (ck == byte.class) {
                        clz = Class.forName(prefix + "B");
                    } else if (ck == short.class) {
                        clz = Class.forName(prefix + "S");
                    } else if (ck == int.class) {
                        clz = Class.forName(prefix + "I");
                    } else if (ck == long.class) {
                        clz = Class.forName(prefix + "J");
                    } else if (ck == float.class) {
                        clz = Class.forName(prefix + "F");
                    } else if (ck == double.class) {
                        clz = Class.forName(prefix + "D");
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }

        return clz;
    }

    public Type getType() {
        return type;
    }

}