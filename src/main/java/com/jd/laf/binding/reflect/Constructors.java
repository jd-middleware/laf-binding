package com.jd.laf.binding.reflect;

import com.jd.laf.binding.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.util.Primitive.inbox;

/**
 * 构造函数工具类
 */
public abstract class Constructors {

    //单参数构造函数映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Constructor>>> singleParameters =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<Class<?>, Option<Constructor>>>();

    //默认构造函数映射
    protected static ConcurrentMap<Class<?>, Option<Constructor>> defaultConstructors =
            new ConcurrentHashMap<Class<?>, Option<Constructor>>();

    protected static ConcurrentMap<Class<?>, List<Constructor>> constructors =
            new ConcurrentHashMap<Class<?>, List<Constructor>>();

    /**
     * 获取构造函数
     *
     * @param type
     * @return
     */
    public static List<Constructor> getConstructors(final Class<?> type) {
        if (type == null || type.isInterface() || type.isArray() || type.isPrimitive()) {
            return null;
        }

        List<Constructor> options = constructors.get(type);
        if (options == null) {
            options = new ArrayList<Constructor>();
            for (Constructor constructor : type.getConstructors()) {
                options.add(constructor);
            }
            List<Constructor> exist = constructors.putIfAbsent(type, options);
            if (exist != null) {
                options = exist;
            }
        }
        return options;
    }

    /**
     * 获取默认构造函数
     *
     * @param type
     * @return
     */
    public static Constructor getDefaultConstructor(final Class<?> type) {
        Option<Constructor> option = defaultConstructors.get(type);
        if (option == null) {
            //获取构造函数
            List<Constructor> constructors = getConstructors(type);
            Class[] parameters;
            Constructor constructor = null;
            //遍历
            if (constructors != null) {
                // 获取公共的实体类的公共构造函数
                int modifiers = type.getModifiers();
                if (Modifier.isPublic(modifiers)
                        && !Modifier.isAbstract(modifiers)
                        && !Modifier.isInterface(modifiers)) {
                    for (Constructor c : constructors) {
                        if (Modifier.isPublic(c.getModifiers())) {
                            parameters = c.getParameterTypes();
                            if (parameters == null || parameters.length == 0) {
                                //单个参数，处理基本类型，如果能直接赋值
                                constructor = c;
                                break;
                            }
                        }
                    }
                }
            }

            option = new Option<Constructor>(constructor);
            Option<Constructor> exist = defaultConstructors.putIfAbsent(type, option);
            if (exist != null) {
                option = exist;
            }
        }
        return option.get();
    }

    /**
     * 获取单个参数的构造函数
     *
     * @param targetType    目标类型
     * @param parameterType 参数类型
     * @return 字段
     */
    public static Constructor getConstructor(final Class<?> targetType, final Class<?> parameterType) {
        ConcurrentMap<Class<?>, Option<Constructor>> options = singleParameters.get(targetType);
        if (options == null) {
            options = new ConcurrentHashMap<Class<?>, Option<Constructor>>();
            ConcurrentMap<Class<?>, Option<Constructor>> exist = singleParameters.putIfAbsent(targetType, options);
            if (exist != null) {
                options = exist;
            }
        }
        //参数已经处理完了基本类型
        Option<Constructor> option = options.get(parameterType);
        if (option == null) {
            //获取构造函数
            List<Constructor> constructors = getConstructors(targetType);
            Class[] parameters;
            Constructor constructor = null;
            //遍历
            if (constructors != null) {
                // 获取公共的实体类的公共构造函数
                int modifiers = targetType.getModifiers();
                if (Modifier.isPublic(modifiers)
                        && !Modifier.isAbstract(modifiers)
                        && !Modifier.isInterface(modifiers)) {
                    for (Constructor c : constructors) {
                        if (Modifier.isPublic(c.getModifiers())) {
                            parameters = c.getParameterTypes();
                            if (parameters.length == 1 && inbox(parameters[0]).isAssignableFrom(parameterType)) {
                                //单个参数，处理基本类型，如果能直接赋值
                                constructor = c;
                                break;
                            }
                        }
                    }
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


}
