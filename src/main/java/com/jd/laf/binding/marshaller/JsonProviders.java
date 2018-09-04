package com.jd.laf.binding.marshaller;

import com.jd.laf.binding.Ordered;

import java.util.*;

/**
 * Json序列化插件管理器
 */
public abstract class JsonProviders {

    //插件集合
    protected static volatile List<JsonProvider> plugins;

    /**
     * 获取Json序列化插件
     *
     * @return
     */
    public static List<JsonProvider> getPlugins() {
        if (plugins == null) {
            //加载插件
            synchronized (JsonProviders.class) {
                if (plugins == null) {
                    List<JsonProvider> result = new ArrayList<JsonProvider>();
                    //加载插件
                    ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class, JsonProviders.class.getClassLoader());
                    for (JsonProvider provider : loader) {
                        result.add(provider);
                    }
                    //降序排序，序号越大，优先级越高
                    Collections.sort(result, new Comparator<JsonProvider>() {
                        @Override
                        public int compare(JsonProvider o1, JsonProvider o2) {
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
     * 获取默认Json序列化插件
     *
     * @return
     */
    public static JsonProvider getPlugin() {
        List<JsonProvider> plugins = getPlugins();
        //获取适合的插件
        return plugins.isEmpty() ? null : plugins.get(0);
    }

}
