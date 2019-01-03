package com.jd.laf.binding.reflect.exception;

/**
 * 反射异常
 */
public class ReflectionException extends Exception {

    public ReflectionException() {
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
