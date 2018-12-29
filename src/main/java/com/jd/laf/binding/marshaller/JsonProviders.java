package com.jd.laf.binding.marshaller;

import static com.jd.laf.binding.Plugin.JSON;

/**
 * Json序列化插件管理器
 */
public abstract class JsonProviders {

    /**
     * 获取默认Json序列化插件
     *
     * @return
     */
    public static JsonProvider getPlugin() {
        return JSON.get();
    }

}
