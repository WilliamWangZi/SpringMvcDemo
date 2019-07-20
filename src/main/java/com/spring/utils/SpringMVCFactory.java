package com.spring.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SpringMVCFactory {

    private static Map<String, Method> methodMap = new HashMap<>();

    private static Map<String, Class> classMap = new HashMap<>();

    public static void putMethod(String url, Method method){
        methodMap.put(url, method);
    }

    public static Method getMethod(String url){
        return methodMap.get(url);
    }

    public static void putController(String url, Class type){
        classMap.put(url, type);
    }

    public static Class getController(String url){
        return classMap.get(url);
    }
}
