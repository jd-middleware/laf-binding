package com.jd.laf.binding.marshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;
import java.lang.annotation.Annotation;

/**
 * XML提供者
 */
public class JaxbProvider implements XmlProvider {

    protected static final JaxbUnmarshaller INSTANCE = new JaxbUnmarshaller();

    @Override
    public Unmarshaller getUnmarshaller() {
        return INSTANCE;
    }

    /**
     * jaxb反序列化
     */
    protected static class JaxbUnmarshaller implements Unmarshaller {
        @Override
        public <T> T unmarshall(String value, Class<T> clazz, String format) throws Exception {
            if (value == null || value.isEmpty()) {
                return null;
            }
            Annotation annotation = clazz.getAnnotation(XmlRootElement.class);
            if (annotation == null) {
                return null;
            }
            JAXBContext context = JAXBContext.newInstance(clazz);
            javax.xml.bind.Unmarshaller marshaller = context.createUnmarshaller();
            return (T) marshaller.unmarshal(new StringReader(value));
        }
    }
}
