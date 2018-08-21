package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.Conversion;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 根据静态工厂方法进行转换
 */
public abstract class StaticMethodSupplier implements ConverterSupplier {

    protected final String methodName;

    public StaticMethodSupplier(String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            throw new IllegalArgumentException("methodName can not be empty.");
        }
        this.methodName = methodName;
    }


    @Override
    public Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        if (sourceType == null || targetType == null) {
            return null;
        }
        // 判断是否有构造函数
        Method method = getMethod(targetType, sourceType, methodName, getCache());
        return method == null ? null : new MethodOperation(method);
    }

    protected abstract ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Method>>> getCache();

    /**
     * 获取工厂方法
     *
     * @param targetType    目标类型
     * @param parameterType 参数类型
     * @param methodName    方法名称
     * @param cache         方法缓存
     * @return 方法
     * @throws SecurityException
     */
    protected static Method getMethod(final Class<?> targetType, final Class<?> parameterType, final String methodName,
                                      ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Method>>> cache) throws SecurityException {
        if (targetType == null || targetType.isInterface() || parameterType == null) {
            return null;
        }
        ConcurrentMap<Class<?>, Option<Method>> options = cache.get(targetType);
        if (options == null) {
            options = new ConcurrentHashMap<Class<?>, Option<Method>>();
            ConcurrentMap<Class<?>, Option<Method>> exist = cache.putIfAbsent(targetType, options);
            if (exist != null) {
                options = exist;
            }
        }
        //参数已经处理完了基本类型
        Option<Method> option = options.get(parameterType);
        if (option == null) {
            //获取构造函数
            Method method = null;
            Method[] methods = targetType.getMethods();
            Class[] parameters;
            //变量构造函数
            for (Method c : methods) {
                parameters = c.getParameterTypes();
                if (Modifier.isStatic(c.getModifiers())
                        && Modifier.isPublic(c.getModifiers())
                        && c.getName().equals(methodName)
                        && targetType.isAssignableFrom(c.getReturnType())
                        && parameters.length == 1 && inbox(parameters[0]).isAssignableFrom(parameterType)) {
                    //单个参数，处理基本类型，如果能直接赋值
                    method = c;
                    break;
                }
            }

            option = new Option<Method>(method);
            Option<Method> exist = options.putIfAbsent(parameterType, option);
            if (exist != null) {
                option = exist;
            }

        }
        return option.get();
    }

    /**
     * 构造函数操作
     */
    protected static final class MethodOperation implements Operation {

        protected final Method method;

        public MethodOperation(Method method) {
            this.method = method;
        }

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            return conversion == null ? null : method.invoke(null, conversion.getSource());
        }
    }

}
