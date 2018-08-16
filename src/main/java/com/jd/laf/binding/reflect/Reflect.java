package com.jd.laf.binding.reflect;

import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.ConverterSupplier.Operation;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.converter.Converters.getOperation;
import static com.jd.laf.binding.reflect.Primitive.inbox;
import static com.jd.laf.binding.reflect.PropertyGetters.getPropertyGetter;

/**
 * 反射工具类
 */
public abstract class Reflect {

    public static final Option EMPTY_OPTION = new Option(null);
    //类的字段名和字段映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<String, Option<Field>>> fields =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<String, Option<Field>>>();

    /**
     * 获取类的字段
     *
     * @param clazz 类
     * @param name  属性名
     * @return 字段
     * @throws ReflectionException
     */
    public static Field getField(final Class<?> clazz, final String name) throws ReflectionException {
        if (clazz == null || clazz.isPrimitive() || name == null || name.isEmpty()) {
            return null;
        }
        ConcurrentMap<String, Option<Field>> options = fields.get(clazz);
        if (options == null) {
            options = new ConcurrentHashMap<String, Option<Field>>();
            ConcurrentMap<String, Option<Field>> exist = fields.putIfAbsent(clazz, options);
            if (exist != null) {
                options = exist;
            }
        }
        Option<Field> option = options.get(name);
        if (option == null) {
            Field field = null;
            SuperClassIterator iterator = new SuperClassIterator(clazz);
            while (iterator.hasNext()) {
                try {
                    field = iterator.next().getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException e) {
                } catch (SecurityException e) {
                    throw new ReflectionException(e.getMessage(), e);
                }
            }
            option = new Option<Field>(field);
            Option<Field> exist = options.putIfAbsent(name, option);
            if (exist != null) {
                option = exist;
            }

        }
        return option.get();
    }

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
        PropertyGetter getter = getPropertyGetter(target.getClass());
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
     * @param target  目标对象
     * @param field   字段
     * @param value   字段值
     * @param format  格式化
     * @param factory 字段访问工厂
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value, final Object format,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || value == null || target == null || factory == null) {
            return true;
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
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || value == null || target == null || factory == null) {
            return true;
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
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value, final Object format,
                              final FieldAccessor accessor) throws ReflectionException {
        if (field == null || value == null || target == null || accessor == null) {
            return true;
        }
        try {
            Option option = convert(field, field.getType(), value.getClass(), value, format);
            if (option != null && option.get() != null) {
                accessor.set(target, option.get());
                return true;
            }
            return false;
        } catch (ReflectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }

    /**
     * 绑定字段
     *
     * @param field      所属字段
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值
     * @param format     格式化信息
     * @throws Exception
     */
    protected static Option convert(final Field field, final Class<?> targetType, final Class<?> sourceType,
                                    final Object value, final Object format) throws Exception {
        if (value == null || targetType == null || sourceType == null) {
            return null;
        }
        Class<?> inboxTargetType = inbox(targetType);
        Class<?> inboxSourceType = inbox(sourceType);
        Option option = null;
        //获取自定义转换器
        Operation operation = getOperation(inboxSourceType, inboxTargetType);
        if (operation != null) {
            option = new Option(operation.execute(new Conversion(inboxSourceType, inboxTargetType, value, format)));
        } else {
            option = String2Array(inboxTargetType, inboxSourceType, value, format);
            if (option == null) {
                option = string2Collection(field, inboxTargetType, inboxSourceType, value, format);
                if (option == null) {
                    option = array2Array(inboxTargetType, inboxSourceType, value, format);
                    if (option == null) {
                        option = array2Collection(field, inboxTargetType, inboxSourceType, value, format);
                        if (option == null) {
                            option = collection2Collection(field, inboxTargetType, inboxSourceType, value, format);
                            if (option == null) {
                                option = collection2Array(field, inboxTargetType, inboxSourceType, value, format);
                            }
                        }
                    }
                }
            }
        }
        return option;
    }

    /**
     * 把字符串转换成数组
     *
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值对象
     * @param format     转换格式化
     * @return 转换后的对象
     * @throws Exception
     */
    protected static Option String2Array(final Class<?> targetType, final Class<?> sourceType, final Object value,
                                         final Object format) throws Exception {
        if (targetType.isArray() && CharSequence.class.isAssignableFrom(sourceType)) {
            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> targetElementType = targetType.getComponentType();
            Class<?> inboxElementType = inbox(targetElementType);
            Operation operation = getOperation(String.class, inboxElementType);
            if (operation != null) {
                //分割字符串
                List<String> parts = split(value.toString(), format == null ? null : format.toString());
                //构建数组
                Object result = Array.newInstance(targetElementType, parts.size());
                int pos = 0;
                //遍历分割后的字符串进行转换
                for (String part : parts) {
                    Array.set(result, pos++, operation.execute(new Conversion(String.class, inboxElementType, part)));
                }
                return new Option(result);
            }
            return EMPTY_OPTION;
        }
        return null;
    }

    /**
     * 把字符串转换成集合
     *
     * @param field      字段
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值对象
     * @param format     转换格式化
     * @return 转换后的对象
     * @throws Exception
     */
    protected static Option string2Collection(final Field field, final Class<?> targetType, final Class<?> sourceType,
                                              final Object value, final Object format) throws Exception {
        if (Collection.class.isAssignableFrom(targetType) && CharSequence.class.isAssignableFrom(sourceType)) {
            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> elementType = inbox(getGenericType(field));
            if (elementType != null) {
                Operation operation = getOperation(String.class, elementType);
                if (operation != null) {
                    //分割字符串
                    List<String> parts = split(value.toString(), format == null ? null : format.toString());
                    Collection result = createCollection(targetType, parts.size());
                    if (result != null) {
                        //遍历分割后的字符串进行转换
                        for (String part : parts) {
                            result.add(operation.execute(new Conversion(String.class, elementType, part)));
                        }
                        return new Option(result);
                    }
                }
            }
            return EMPTY_OPTION;
        }
        return null;
    }

    /**
     * 数组转换成数组
     *
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值对象
     * @param format     格式化
     * @return 成功标识
     * @throws Exception
     */
    protected static Option array2Array(final Class<?> targetType, final Class<?> sourceType,
                                        final Object value, final Object format) throws Exception {
        if (targetType.isArray() && sourceType.isArray()) {
            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> targetElementType = targetType.getComponentType();
            Class<?> inboxTargetElementType = inbox(targetElementType);
            Class<?> inboxSourceElementType = inbox(sourceType.getComponentType());
            if (inboxTargetElementType != null) {
                if (inboxTargetElementType.equals(inboxSourceElementType)) {
                    return new Option(value);
                }
                //数组大小
                int size = Array.getLength(value);
                //构建数组
                Object result = Array.newInstance(targetElementType, size);
                Option option;
                Object element;
                //遍历数组
                for (int i = 0; i < size; i++) {
                    //递归转换元素
                    element = Array.get(value, i);
                    inboxSourceElementType = inboxSourceElementType != null ? inboxSourceElementType : (element == null ? null : element.getClass());
                    option = convert(null, inboxTargetElementType, inboxSourceElementType, element, format);
                    if (option == null) {
                        return EMPTY_OPTION;
                    }
                    Array.set(result, i, option.get());
                }
                return new Option(result);
            }
            return EMPTY_OPTION;
        }
        return null;
    }

    /**
     * 数组转换成集合
     *
     * @param field      字段
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值对象
     * @param format     格式化
     * @return 成功标识
     * @throws Exception
     */
    protected static Option array2Collection(final Field field, final Class<?> targetType, final Class<?> sourceType,
                                             final Object value, final Object format) throws Exception {
        if (Collection.class.isAssignableFrom(targetType) && sourceType.isArray()) {
            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> targetElementType = inbox(getGenericType(field));
            if (targetElementType != null) {
                Class<?> sourceElementType = sourceType.getComponentType();
                //数组大小
                int size = Array.getLength(value);
                Collection result = createCollection(targetType, size);
                if (result != null) {
                    Option option;
                    Object element;
                    //遍历数组
                    for (int i = 0; i < size; i++) {
                        //递归转换元素
                        element = Array.get(value, i);
                        sourceElementType = sourceElementType != null ? sourceElementType : (element == null ? null : element.getClass());
                        option = convert(null, targetElementType, sourceElementType, element, format);
                        if (option == null) {
                            return EMPTY_OPTION;
                        }
                        result.add(option.get());
                    }
                    return new Option(result);
                }
            }
            return EMPTY_OPTION;
        }
        return null;
    }

    /**
     * 集合转换成集合
     *
     * @param field      字段
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值对象
     * @param format     格式化
     * @return 成功标识
     * @throws Exception
     */
    protected static Option collection2Collection(final Field field, final Class<?> targetType, final Class<?> sourceType,
                                                  final Object value, final Object format) throws Exception {
        if (Collection.class.isAssignableFrom(targetType) && Collection.class.isAssignableFrom(sourceType)) {
            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> targetElementType = inbox(getGenericType(field));
            if (targetElementType != null) {
                Class<?> sourceElementType;
                //集合大小
                int size = ((Collection) value).size();
                Collection result = createCollection(targetType, size);
                if (result != null) {
                    Option option;
                    for (Object v : ((Collection) value)) {
                        sourceElementType = v != null ? v.getClass() : null;
                        //递归转换元素
                        option = convert(null, targetElementType, sourceElementType, v, format);
                        if (option == null) {
                            return EMPTY_OPTION;
                        }
                        result.add(option.get());
                    }
                    return new Option(result);
                }
            }
            return EMPTY_OPTION;
        }
        return null;
    }

    /**
     * 集合转换成数组
     *
     * @param field      字段
     * @param targetType 目标类型
     * @param sourceType 原始类型
     * @param value      值对象
     * @param format     格式化
     * @return 成功标识
     * @throws Exception
     */
    protected static Option collection2Array(final Field field, final Class<?> targetType, final Class<?> sourceType,
                                             final Object value, final Object format) throws Exception {
        if (targetType.isArray() && Collection.class.isAssignableFrom(sourceType)) {
            //数据是文本，如果数组的元素支持转换，则分割文本进行转换
            Class<?> targetElementType = targetType.getComponentType();
            Class<?> inboxTargetElementType = inbox(targetElementType);
            if (inboxTargetElementType != null) {
                Class<?> sourceElementType;
                //集合大小
                int size = ((Collection) value).size();
                Object result = Array.newInstance(targetElementType, size);
                Option option;
                int count = 0;
                for (Object v : ((Collection) value)) {
                    sourceElementType = v != null ? v.getClass() : null;
                    //递归转换元素
                    option = convert(null, inboxTargetElementType, sourceElementType, v, format);
                    if (option == null) {
                        return EMPTY_OPTION;
                    }
                    Array.set(result, count++, option.get());
                }
                return new Option(result);
            }
            return EMPTY_OPTION;
        }
        return null;
    }


    /**
     * 创建集合对象
     *
     * @param targetType
     * @param size
     * @return
     * @throws Exception
     */
    protected static Collection createCollection(Class<?> targetType, int size) throws Exception {
        if (targetType.equals(List.class)) {
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

    /**
     * 按照字符串分割
     *
     * @param value
     * @param delimiter
     * @return
     */
    protected static List<String> split(final String value, final String delimiter) {
        List<String> result = new LinkedList<String>();
        if (delimiter == null || delimiter.isEmpty()) {
            return result;
        } else if (delimiter.length() == 1) {
            return split(value, delimiter.charAt(0));
        }
        int length = value.length();
        int maxPos = delimiter.length() - 1;
        int start = 0;
        int pos = 0;
        int end = 0;
        for (int i = 0; i < length; i++) {
            if (value.charAt(i) == delimiter.charAt(pos)) {
                if (pos++ == maxPos) {
                    if (end > start) {
                        result.add(value.substring(start, end + 1));
                    }
                    pos = 0;
                    start = i + 1;
                }
            } else {
                end = i;
            }
        }
        if (start < length) {
            result.add(value.substring(start, length));
        }
        return result;
    }

    /**
     * 按照字符分割
     *
     * @param value
     * @param delimiter
     * @return
     */
    protected static List<String> split(final String value, final char delimiter) {
        List<String> result = new LinkedList<String>();
        int len = value.length();
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (value.charAt(i) == delimiter) {
                if (i > start) {
                    result.add(value.substring(start, i));
                }
                start = i + 1;
            }
        }
        if (start < len) {
            result.add(value.substring(start, len));
        }
        return result;
    }

    /**
     * 获取泛型
     *
     * @param field
     * @return
     */
    protected static Class getGenericType(final Field field) {
        if (field == null) {
            return null;
        }
        Type type = field.getGenericType();
        if (type == null) {
            return null;
        }
        // 如果是泛型参数的类型
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            //得到泛型里的class类型对象
            type = pt.getActualTypeArguments()[0];
            if (type instanceof Class) {
                return (Class) type;
            }
        }
        return null;
    }

}
