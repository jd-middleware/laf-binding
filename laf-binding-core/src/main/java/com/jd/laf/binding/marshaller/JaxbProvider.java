package com.jd.laf.binding.marshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * XML提供者
 */
public class JaxbProvider implements XmlProvider {

    protected static final ConcurrentMap<Class, JAXBContext> contexts = new ConcurrentHashMap<Class, JAXBContext>();

    @Override
    public Unmarshaller getUnmarshaller() {
        return JaxbUnmarshaller.INSTANCE;
    }

    @Override
    public Marshaller getMarshaller() {
        return JaxbMarshaller.INSTANCE;
    }

    /**
     * 获取JAXB上下文
     *
     * @param clazz
     * @return
     * @throws JAXBException
     */
    protected static JAXBContext getJaxbContext(final Class clazz) throws JAXBException {
        if (clazz == null) {
            return null;
        }
        JAXBContext context = contexts.get(clazz);
        if (context == null) {
            context = JAXBContext.newInstance(clazz);
            JAXBContext exists = contexts.putIfAbsent(clazz, context);
            if (exists != null) {
                context = exists;
            }
        }
        return context;
    }

    /**
     * jaxb反序列化
     */
    protected static class JaxbUnmarshaller implements Unmarshaller {

        public static final Unmarshaller INSTANCE = new JaxbUnmarshaller();

        @Override
        public <T> T unmarshall(final String value, final Class<T> clazz, final String format) throws Exception {
            return unmarshall(value, clazz);
        }

        @Override
        public <T> T unmarshall(final String value, final Class<T> clazz) throws Exception {
            if (value == null || value.isEmpty()) {
                return null;
            }
            Annotation annotation = clazz.getAnnotation(XmlRootElement.class);
            if (annotation == null) {
                return null;
            }
            JAXBContext context = getJaxbContext(clazz);
            javax.xml.bind.Unmarshaller marshaller = context.createUnmarshaller();
            return (T) marshaller.unmarshal(new StringReader(value));
        }

        @Override
        public <T> T unmarshall(final String value, final TypeReference<T> reference, final String format) throws Exception {
            return unmarshall(value, (Class<T>) reference.getType());
        }

        @Override
        public <T> T unmarshall(final String value, final TypeReference<T> reference) throws Exception {
            return unmarshall(value, (Class<T>) reference.getType());
        }
    }


    /**
     * jaxb反序列化
     */
    protected static class JaxbMarshaller implements Marshaller {

        protected static final Marshaller INSTANCE = new JaxbMarshaller();

        @Override
        public String marshall(final Object target) throws Exception {
            if (target == null) {
                return null;
            }
            JAXBContext context = getJaxbContext(target.getClass());
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(target, writer);
            return writer.toString();
        }
    }
}
