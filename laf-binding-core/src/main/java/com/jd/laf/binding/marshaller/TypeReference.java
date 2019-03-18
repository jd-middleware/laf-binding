package com.jd.laf.binding.marshaller;

import com.jd.laf.binding.util.Function;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.util.Collections.computeIfAbsent;

/**
 * 来源于fastjson，便于插件实现
 *
 * @param <T>
 * @see com.alibaba.fastjson.TypeReference
 */
public abstract class TypeReference<T> {
    protected static ConcurrentMap<Type, Type> classTypeCache = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);
    public final static Type LIST_STRING = new TypeReference<List<String>>() {
    }.getType();

    protected final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        this.type = computeIfAbsent(classTypeCache, type, new Function<Type, Type>() {
            @Override
            public Type apply(Type key) {
                return key;
            }
        });
    }

    protected TypeReference(final Type... actualTypeArguments) {
        Class<?> thisClass = this.getClass();
        Type superClass = thisClass.getGenericSuperclass();

        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        Type rawType = argType.getRawType();
        Type[] argTypes = argType.getActualTypeArguments();

        int actualIndex = 0;
        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable && actualIndex < actualTypeArguments.length) {
                argTypes[i] = actualTypeArguments[actualIndex++];
            }
            // fix for openjdk and android env
            if (argTypes[i] instanceof GenericArrayType) {
                argTypes[i] = checkPrimitiveArray((GenericArrayType) argTypes[i]);
            }

            // 如果有多层泛型且该泛型已经注明实现的情况下，判断该泛型下一层是否还有泛型
            if (argTypes[i] instanceof ParameterizedType) {
                argTypes[i] = handle((ParameterizedType) argTypes[i], actualTypeArguments, actualIndex);
            }
        }

        Type key = new MyParameterizedType(argTypes, thisClass, rawType);
        this.type = computeIfAbsent(classTypeCache, key, new Function<Type, Type>() {
            @Override
            public Type apply(Type key) {
                return key;
            }
        });
    }

    protected Type handle(ParameterizedType type, Type[] actualTypeArguments, int actualIndex) {
        Class<?> thisClass = this.getClass();
        Type rawType = type.getRawType();
        Type[] argTypes = type.getActualTypeArguments();

        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable && actualIndex < actualTypeArguments.length) {
                argTypes[i] = actualTypeArguments[actualIndex++];
            }

            // fix for openjdk and android env
            if (argTypes[i] instanceof GenericArrayType) {
                argTypes[i] = checkPrimitiveArray((GenericArrayType) argTypes[i]);
            }

            // 如果有多层泛型且该泛型已经注明实现的情况下，判断该泛型下一层是否还有泛型
            if (argTypes[i] instanceof ParameterizedType) {
                return handle((ParameterizedType) argTypes[i], actualTypeArguments, actualIndex);
            }
        }

        Type key = new MyParameterizedType(argTypes, thisClass, rawType);
        return key;
    }

    protected Type checkPrimitiveArray(final GenericArrayType genericArrayType) {
        Type clz = genericArrayType;
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

    /**
     * Gets underlying {@code Type} instance.
     */
    public Type getType() {
        return type;
    }

    /**
     * 参数类型
     */
    public static class MyParameterizedType implements ParameterizedType {

        private final Type[] actualTypeArguments;
        private final Type ownerType;
        private final Type rawType;

        public MyParameterizedType(Type[] actualTypeArguments, Type ownerType, Type rawType) {
            this.actualTypeArguments = actualTypeArguments;
            this.ownerType = ownerType;
            this.rawType = rawType;
        }

        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        public Type getOwnerType() {
            return ownerType;
        }

        public Type getRawType() {
            return rawType;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MyParameterizedType that = (MyParameterizedType) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(actualTypeArguments, that.actualTypeArguments)) {
                return false;
            } else if (ownerType != null ? !ownerType.equals(that.ownerType) : that.ownerType != null) {
                return false;
            }
            return rawType != null ? rawType.equals(that.rawType) : that.rawType == null;

        }

        @Override
        public int hashCode() {
            int result = actualTypeArguments != null ? Arrays.hashCode(actualTypeArguments) : 0;
            result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
            result = 31 * result + (rawType != null ? rawType.hashCode() : 0);
            return result;
        }
    }
}