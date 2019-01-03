package com.jd.laf.binding.util;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 集合工具类
 */
public abstract class Collections {

    /**
     * 创建集合对象
     *
     * @param targetType
     * @param size
     * @return
     * @throws Exception
     */
    public static Collection create(final Class<?> targetType, final int size) throws Exception {
        if (targetType == null) {
            return null;
        } else if (targetType.equals(List.class)) {
            return new ArrayList(size);
        } else if (targetType.equals(Set.class)) {
            return new HashSet(size);
        } else if (targetType.equals(SortedSet.class)) {
            return new TreeSet();
        } else if (targetType.isInterface()) {
            // 接口
            return null;
        } else if (Modifier.isAbstract(targetType.getModifiers())) {
            //抽象方法
            return null;
        } else {
            return (Collection) targetType.newInstance();
        }
    }
}
