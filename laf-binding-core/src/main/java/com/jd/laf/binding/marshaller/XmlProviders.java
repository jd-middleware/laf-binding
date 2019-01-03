package com.jd.laf.binding.marshaller;

import static com.jd.laf.binding.Plugin.XML;

/**
 * Xml序列化插件管理器
 */
@Deprecated
public abstract class XmlProviders {

    /**
     * 获取默认Xml序列化插件
     *
     * @return
     */
    public static XmlProvider getPlugin() {
        return XML.get();
    }

}
