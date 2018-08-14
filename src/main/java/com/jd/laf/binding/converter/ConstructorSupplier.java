package com.jd.laf.binding.converter;

import com.jd.laf.binding.Option;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.reflect.Primitive.inbox;

/**
 * 根据构造函数进行转换
 */
public class ConstructorSupplier implements ConverterSupplier {

    //单参数构造函数映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Constructor>>> constructors =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<Class<?>, Option<Constructor>>>();


    @Override
    public Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        if (sourceType == null || targetType == null) {
            return null;
        }
        // 判断是否有构造函数
        Constructor constructor = getConstructor(targetType, sourceType);
        return constructor == null ? null : new ConstructorOperation(constructor);
    }

    @Override
    public int order() {
        return 1;
    }

    /**
     * 获取单个参数的构造函数
     *
     * @param targetType    目标类型
     * @param parameterType 参数类型
     * @return 字段
     * @throws SecurityException
     */
    protected static Constructor getConstructor(final Class<?> targetType, final Class<?> parameterType) throws SecurityException {
        if (targetType == null || targetType.isInterface() || parameterType == null) {
            return null;
        }
        ConcurrentMap<Class<?>, Option<Constructor>> options = constructors.get(targetType);
        if (options == null) {
            options = new ConcurrentHashMap<Class<?>, Option<Constructor>>();
            ConcurrentMap<Class<?>, Option<Constructor>> exist = constructors.putIfAbsent(targetType, options);
            if (exist != null) {
                options = exist;
            }
        }
        //参数已经处理完了基本类型
        Option<Constructor> option = options.get(parameterType);
        if (option == null) {
            //获取构造函数
            Constructor constructor = null;
            Constructor[] constructors = targetType.getConstructors();
            Class[] parameters;
            //变量构造函数
            for (Constructor c : constructors) {
                parameters = c.getParameterTypes();
                if (parameters.length == 1 && inbox(parameters[0]).isAssignableFrom(parameterType)) {
                    //单个参数，处理基本类型，如果能直接赋值
                    constructor = c;
                    break;
                }
            }

            option = new Option<Constructor>(constructor);
            Option<Constructor> exist = options.putIfAbsent(parameterType, option);
            if (exist != null) {
                option = exist;
            }

        }
        return option.get();
    }

    /**
     * 构造函数操作
     */
    protected static final class ConstructorOperation implements Operation {

        protected final Constructor constructor;

        public ConstructorOperation(Constructor constructor) {
            this.constructor = constructor;
        }

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            return conversion == null ? null : constructor.newInstance(conversion.source);
        }
    }

}
