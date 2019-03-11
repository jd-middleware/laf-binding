package com.jd.laf.binding;

import com.jd.laf.binding.binder.Binder;
import com.jd.laf.binding.converter.*;
import com.jd.laf.binding.marshaller.JsonProvider;
import com.jd.laf.binding.marshaller.XmlProvider;
import com.jd.laf.binding.reflect.FieldAccessorFactory;
import com.jd.laf.binding.reflect.MethodFactory;
import com.jd.laf.binding.reflect.PropertySupplier;
import com.jd.laf.binding.reflect.PropertySupplier.MethodSupplier;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;
import com.jd.laf.extension.*;
import com.jd.laf.extension.Selector.CacheSelector;
import com.jd.laf.extension.Selector.ConverterSelector;
import com.jd.laf.extension.Selector.MatchSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.jd.laf.binding.converter.Converter.NONE;

/**
 * 插件接口
 */
public interface Plugin {

    String GET_OBJECT = "getObject";

    ExtensionPoint<MethodFactory, String> METHOD_FACTORY = new ExtensionPointLazy<MethodFactory, String>(MethodFactory.class);

    ExtensionPoint<JsonProvider, String> JSON = new ExtensionPointLazy(JsonProvider.class);

    ExtensionPoint<XmlProvider, String> XML = new ExtensionPointLazy(XmlProvider.class);

    ExtensionPoint<Binder, Class<?>> BINDER = new ExtensionPointLazy(Binder.class, new Classify<Binder, Class<?>>() {
        @Override
        public Class<?> type(Binder obj) {
            return obj.annotation();
        }
    });

    /**
     * 字段访问插件
     */
    ExtensionPoint<FieldAccessorFactory, String> FIELD = new ExtensionPointLazy<FieldAccessorFactory, String>(FieldAccessorFactory.class);

    //注解转换选择器
    ExtensionSelector<Transformer, Class<?>, ConversionType, Transformer> TRANSFORMER = new ExtensionSelector<Transformer, Class<?>, ConversionType, Transformer>(
            new ExtensionPointLazy(Transformer.class, new Classify<Transformer, Class<?>>() {
                @Override
                public Class<?> type(Transformer obj) {
                    return obj.annotation();
                }
            }),
            new CacheSelector(new Selector<Transformer, Class<?>, ConversionType, Transformer>() {
                @Override
                public Transformer select(final ExtensionPoint<Transformer, Class<?>> extensions,
                                          final ConversionType condition) {
                    Annotation[] annotations = condition.scope.getAnnotations();

                    ExtensionMeta<Transformer, Class<?>> meta;
                    Transformer transformer;
                    for (Annotation annotation : annotations) {
                        meta = extensions.meta(annotation.annotationType());
                        if (meta != null) {
                            transformer = meta.getTarget();
                            if (transformer != null && transformer.support(condition.sourceType, condition.targetType)) {
                                return transformer;
                            }
                        }

                    }
                    return null;
                }
            })
    );

    //转换选择器
    ExtensionSelector<ConverterSupplier, String, ConversionType, Converter> CONVERTER = new ExtensionSelector<ConverterSupplier, String, ConversionType, Converter>(
            new ExtensionPointLazy<ConverterSupplier, String>(ConverterSupplier.class),
            new CacheSelector<ConverterSupplier, String, ConversionType, Converter>(new ConverterSelector<ConverterSupplier, String, ConversionType, Converter>() {
                @Override
                protected Converter convert(final ConverterSupplier target, final ConversionType condition) {
                    return target.getConverter(condition);
                }
            }) {
                @Override
                protected Converter before(final ConversionType condition) {
                    if (condition.targetType == condition.sourceType || condition.targetType.isAssignableFrom(condition.sourceType)) {
                        //可以直接赋值
                        return NONE;
                    }
                    return null;
                }
            });

    //属性提供者选择器
    ExtensionSelector<PropertySupplier, String, Class<?>, PropertySupplier> PROPERTY = new ExtensionSelector<PropertySupplier, String, Class<?>, PropertySupplier>(
            new ExtensionPointLazy<PropertySupplier, String>(PropertySupplier.class),
            new CacheSelector<PropertySupplier, String, Class<?>,
                    PropertySupplier>(new MatchSelector<PropertySupplier, String, Class<?>>() {
                @Override
                protected boolean match(PropertySupplier target, Class<?> condition) {
                    return target.support(condition);
                }
            }) {
                @Override
                protected PropertySupplier fail(final Class<?> condition) {
                    //查找方法Object get(String name)
                    try {
                        Method method = condition.getMethod(GET_OBJECT, String.class);
                        if (method != null) {
                            return new MethodSupplier(method);
                        }
                    } catch (Exception e) {
                    }
                    return null;
                }
            });

    //数组提供者选择器
    ExtensionSelector<ArraySupplier, String, Class<?>, ArraySupplier> ARRAY = new ExtensionSelector<ArraySupplier, String, Class<?>, ArraySupplier>(
            new ExtensionPointLazy<ArraySupplier, String>(ArraySupplier.class),
            new CacheSelector(new MatchSelector<ArraySupplier, String, Class<?>>() {
                @Override
                protected boolean match(final ArraySupplier target, final Class<?> condition) {
                    return target.support(condition);
                }
            }));

    //简单转换选择器
    ExtensionSelector<SimpleConverter, Class<?>, ConversionType, Converter> SIMPLE = new ExtensionSelector<SimpleConverter, Class<?>, ConversionType, Converter>(
            new ExtensionPointLazy<SimpleConverter, Class<?>>(SimpleConverter.class, new Classify<SimpleConverter, Class<?>>() {
                @Override
                public Class<?> type(SimpleConverter obj) {
                    return obj.targetType();
                }
            }),
            new CacheSelector<SimpleConverter, Class<?>, ConversionType, Converter>(new Selector<SimpleConverter, Class<?>, ConversionType, Converter>() {
                @Override
                public Converter select(final ExtensionPoint<SimpleConverter, Class<?>> extensions, final ConversionType condition) {
                    Iterable<ExtensionMeta<SimpleConverter, Class<?>>> metas = extensions.metas(condition.targetType);
                    if (metas != null) {
                        SimpleConverter converter;
                        for (ExtensionMeta<SimpleConverter, Class<?>> meta : metas) {
                            converter = meta.getTarget();
                            if (converter != null && converter.support(condition.sourceType)) {
                                return converter;
                            }
                        }
                    }
                    return null;
                }
            })
    );
}
