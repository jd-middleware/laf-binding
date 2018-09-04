package com.jd.laf.binding.marshaller;

import com.jd.laf.binding.Ordered;

import java.util.*;

/**
 * Xml序列化插件管理器
 */
public abstract class XmlProviders {

    //插件集合
    protected static volatile List<XmlProvider> plugins;

    /**
     * 获取Xml序列化插件
     *
     * @return
     */
    public static List<XmlProvider> getPlugins() {
        if (plugins == null) {
            //加载插件
            synchronized (XmlProviders.class) {
                if (plugins == null) {
                    List<XmlProvider> result = new ArrayList<XmlProvider>();
                    //加载插件
                    ServiceLoader<XmlProvider> loader = ServiceLoader.load(XmlProvider.class, XmlProviders.class.getClassLoader());
                    for (XmlProvider provider : loader) {
                        result.add(provider);
                    }
                    //降序排序，序号越大，优先级越高
                    Collections.sort(result, new Comparator<XmlProvider>() {
                        @Override
                        public int compare(XmlProvider o1, XmlProvider o2) {
                            int order1 = o1 != null && o1 instanceof Ordered ? ((Ordered) o1).order() : 0;
                            int order2 = o2 != null && o2 instanceof Ordered ? ((Ordered) o2).order() : 0;
                            return order2 - order1;
                        }
                    });
                    plugins = result;
                }
            }
        }

        //获取适合的插件
        return plugins;
    }

    /**
     * 获取默认Xml序列化插件
     *
     * @return
     */
    public static XmlProvider getPlugin() {
        List<XmlProvider> plugins = getPlugins();
        //获取适合的插件
        return plugins.isEmpty() ? null : plugins.get(0);
    }

}
