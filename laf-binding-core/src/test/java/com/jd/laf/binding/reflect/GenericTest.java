package com.jd.laf.binding.reflect;

import com.jd.laf.extension.Option;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class GenericTest {

    @Test
    public void testGeneric() throws NoSuchMethodException {

        EchoService<Integer> echoService = new EchoService2();

        Class clazz = echoService.getClass();

        GenericClass classGeneric = Generics.get(clazz);
        Method[] methods;
        LinkedList<Class> queue = new LinkedList<Class>();
        queue.offer(clazz);
        while (!queue.isEmpty()) {
            clazz = queue.poll();

            methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println("method: " + method.toString());
                int i = 0;
                for (GenericMeta[] infos : classGeneric.get(method)) {
                    System.out.println("  parameter-" + (i++));
                    for (GenericMeta info : infos) {
                        System.out.println("    " + info);
                    }
                }
            }

            clazz = clazz.getSuperclass();
            if (clazz != Object.class) {
                //
                queue.offer(clazz);
            }
        }


    }


    public interface Hello<T> {

        void hello(T message);
    }

    public static class EchoService<T> implements Hello<T> {

        public <M> M echo(M message, Class<M> clazz) {
            return message;
        }

        public T echo2(T message) {
            return message;
        }

        public String echo3(Option<String> message) {
            return message.get();
        }

        @Override
        public void hello(T message) {

        }
    }

    public static class EchoService1<M, T> extends EchoService<T> {

        public void hello2(List<M> message, int v) {

        }
    }

    public static class EchoService2<T> extends EchoService1<String, T> {

        @Override
        public void hello(T message) {
            super.hello(message);
        }
    }

}
