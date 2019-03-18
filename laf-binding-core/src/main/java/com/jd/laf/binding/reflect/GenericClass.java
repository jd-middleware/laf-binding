package com.jd.laf.binding.reflect;

import com.jd.laf.binding.util.Function;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.util.Collections.computeIfAbsent;

/**
 * 类型的泛型信息
 */
public class GenericClass {

    protected Class clazz;

    //父类的泛型信息
    protected Map<Class, Map<String, GenericMeta>> classGeneric = new HashMap<Class, Map<String, GenericMeta>>();
    //字段的泛型信息
    protected ConcurrentMap<Field, GenericMeta[]> fieldGeneric = new ConcurrentHashMap<Field, GenericMeta[]>();
    //参数泛型信息
    protected ConcurrentMap<Method, GenericMeta[][]> methodGeneric = new ConcurrentHashMap<Method, GenericMeta[][]>();

    public GenericClass(Class clazz) {
        this.clazz = clazz;

        //当前类型声明的泛型
        String name;
        Map<String, GenericMeta> childMap = new HashMap<String, GenericMeta>(3);
        TypeVariable<Class>[] variables = clazz.getTypeParameters();
        if (variables.length > 0) {
            for (TypeVariable<Class> variable : variables) {
                name = variable.toString();
                childMap.put(name, new GenericMeta(name, clazz));
            }
            classGeneric.put(clazz, childMap);
        }

        LinkedList<Class> queue = new LinkedList<Class>();
        queue.offer(clazz);
        Class parent;
        Map<String, GenericMeta> parentMap;
        while (!queue.isEmpty()) {
            clazz = queue.poll();
            //父类
            parent = clazz.getSuperclass();
            if (parent != Object.class) {
                //存储父类的泛型信息
                parentMap = new HashMap<String, GenericMeta>(3);
                //父类的泛型
                variables = parent.getTypeParameters();
                //通过子类获取父类泛型的具体类型
                Type type = clazz.getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                    //得到泛型里的class类型对象
                    Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                    Type argument;
                    for (int i = 0; i < arguments.length; i++) {
                        argument = arguments[i];
                        name = variables[i].getName();
                        if (argument instanceof Class) {
                            parentMap.put(name, new GenericMeta(name, (Class) argument));
                        } else if (argument instanceof TypeVariable) {
                            GenericDeclaration gd = ((TypeVariable) argument).getGenericDeclaration();
                            if (gd instanceof Class) {
                                //获取子类的定义
                                parentMap.put(name, childMap.get(name));
                            }
                        }
                    }
                }
                //父类的泛型信息
                classGeneric.put(parent, parentMap);
                childMap = parentMap;
                queue.offer(parent);
            }
        }
    }

    /**
     * 获取字段泛型
     *
     * @param field
     * @return
     */
    public GenericMeta[] get(final Field field) {
        if (field == null) {
            return null;
        }

        return computeIfAbsent(fieldGeneric, field, new Function<Field, GenericMeta[]>() {
            @Override
            public GenericMeta[] apply(final Field key) {
                return compute(field.getGenericType(), field.getType(), field.getDeclaringClass());
            }
        });
    }

    /**
     * 获取方法泛型
     *
     * @param method
     * @return
     */
    public GenericMeta[][] get(final Method method) {
        if (method == null) {
            return null;
        }
        return computeIfAbsent(methodGeneric, method,
                new Function<Method, GenericMeta[][]>() {
                    @Override
                    public GenericMeta[][] apply(final Method method) {
                        Class<?> owner = method.getDeclaringClass();
                        Class<?>[] classes = method.getParameterTypes();
                        Type[] types = method.getGenericParameterTypes();

                        GenericMeta[][] result = new GenericMeta[classes.length][];
                        GenericMeta[] infos;
                        List<Integer> classParameters = new LinkedList<Integer>();
                        for (int i = 0; i < classes.length; i++) {
                            infos = compute(types[i], classes[i], owner);
                            if (infos.length == 1 && classes[i] == Class.class) {
                                //查看方法参数有类的泛型声明，如 public M int xxxx(M obj,Class<M> clazz);
                                classParameters.add(i);
                            }
                            result[i] = infos;
                        }

                        GenericMeta info;
                        GenericMeta classParameterInfo;
                        for (Integer classParameter : classParameters) {
                            classParameterInfo = result[classParameter][0];
                            for (int i = 0; i < result.length; i++) {
                                if (i != classParameter) {
                                    infos = result[i];
                                    for (int k = 0; k < infos.length; k++) {
                                        info = infos[k];
                                        if (info.getName().equals(classParameterInfo.name)) {
                                            info.setClassParameter(classParameter);
                                        }
                                    }
                                }
                            }
                        }

                        return result;
                    }
                });
    }

    /**
     * 获取泛型
     *
     * @param type
     * @param scopeType
     * @param owner
     * @return
     */
    protected GenericMeta[] compute(final Type type, final Class scopeType, final Class owner) {
        String name;
        List<GenericMeta> result = new LinkedList<GenericMeta>();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            //得到泛型里的class类型对象
            Type[] arguments = pType.getActualTypeArguments();
            Type argument;
            for (int i = 0; i < arguments.length; i++) {
                argument = arguments[i];
                name = argument.toString();
                if (argument instanceof Class) {
                    result.add(new GenericMeta(name, scopeType, (Class) argument));
                } else if (argument instanceof TypeVariable) {
                    GenericDeclaration gd = ((TypeVariable) argument).getGenericDeclaration();
                    if (gd instanceof Class) {
                        result.add(new GenericMeta(classGeneric.get(owner).get(name), scopeType));
                    } else if (gd instanceof Method) {
                        result.add(new GenericMeta(name));
                    }
                }
            }
        } else if (type instanceof TypeVariable) {
            name = type.toString();
            GenericDeclaration gd = ((TypeVariable) type).getGenericDeclaration();
            if (gd instanceof Class) {
                result.add(classGeneric.get(owner).get(name));
            } else if (gd instanceof Method) {
                result.add(new GenericMeta(name));
            }
        }
        return result.toArray(new GenericMeta[result.size()]);
    }
}