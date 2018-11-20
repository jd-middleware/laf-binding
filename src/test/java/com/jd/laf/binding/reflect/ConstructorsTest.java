package com.jd.laf.binding.reflect;

import org.junit.Assert;
import org.junit.Test;

public class ConstructorsTest {

    @Test
    public void testConstructor() {

        Assert.assertNull(Constructors.getDefaultConstructor(AbstractClass.class));
        Assert.assertNull(Constructors.getDefaultConstructor(PublicInterface.class));
        Assert.assertNull(Constructors.getDefaultConstructor(PublicNoDefaultConstructorClass.class));
        Assert.assertNotNull(Constructors.getConstructor(PublicClass.class, String.class));
        Assert.assertNull(Constructors.getDefaultConstructor(InnerClass.Inner2.class));

    }

    public static abstract class AbstractClass {
    }

    public static class PublicClass {

        private String name;

        public PublicClass(String name) {
            this.name = name;
        }
    }

    public interface PublicInterface {
    }

    public static class PublicNoDefaultConstructorClass {

        private String name;

        public PublicNoDefaultConstructorClass(String name) {
            this.name = name;
        }
    }

    public static class InnerClass {

        public class Inner2 {

        }
    }

}
