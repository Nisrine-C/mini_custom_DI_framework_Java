package com.chakir;

import annotations.ComponentScan;
import annotations.Configuration;
import annotations.SimpleComponent;
import annotations.SimpleAutoWired;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AppContext{
    Map<Class<?>, Object> objectRegistryMap = new HashMap<>();

    AppContext(Class<?> clazz) throws Exception {
        initializeContext(clazz);
    }
    private void initializeContext(Class<?> clazz) throws Exception{
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            throw new RuntimeException("Please provide a valid configuration file!");
        } else {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            String packageValue = componentScan.value();
            Reflections reflections = new Reflections(packageValue);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(SimpleComponent.class);

            for (Class<?> loadingClass: classes) {
                try {
                    if (loadingClass.isAnnotationPresent(SimpleComponent.class)) {
                        registerClass(loadingClass);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public <T> T getInstance(Class<T> clazz) throws Exception {
        if(!objectRegistryMap.containsKey(clazz)){
            throw new RuntimeException("Class now found " + clazz.getName());
        }
        T object = (T) objectRegistryMap.get(clazz);
        //Field[] declaredFields = clazz.getDeclaredFields();
        //injectAnnotatedFields(object, declaredFields);
        performInjection(object);
        return object;
    }

    private <T> void performInjection(T object) throws Exception {
        for(Field field : object.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(SimpleAutoWired.class)) {
                injectField(object,field);
            }
        }
        for (Method method : object.getClass().getDeclaredMethods()){
            if (method.isAnnotationPresent(SimpleAutoWired.class)){
                injectMethod(object,method);
            }
        }
    }
    private <T> void injectField(T object,Field field) throws Exception{
        field.setAccessible(true);
        Class<?> type = field.getType();
        Object innerObject = getDependency(type);
        field.set(object, innerObject);
    }

    private <T> void injectMethod(T object,Method method) throws Exception{
        method.setAccessible(true);
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for(int i = 0; i < paramTypes.length;i++){
            args[i] = getDependency(paramTypes[i]);
        }
        method.invoke(object,args);
    }

    private Object getDependency(Class<?> dependencyType) throws Exception {
        if (!objectRegistryMap.containsKey(dependencyType)) {
            throw new RuntimeException("Dependency not found: " + dependencyType.getName());
        }
        return objectRegistryMap.get(dependencyType);
    }

    private void registerClass(Class<?> clazz) throws Exception {
        Constructor<?> autowiredConstructor = findAutowiredConstructor(clazz);

        Object instance;
        if (autowiredConstructor != null) {
            Class<?>[] paramTypes = autowiredConstructor.getParameterTypes();
            Object[] args = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
                args[i] = getDependency(paramTypes[i]);
            }

            instance = autowiredConstructor.newInstance(args);
        } else {
            instance = clazz.getDeclaredConstructor().newInstance();
        }

        objectRegistryMap.put(clazz, instance);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(SimpleAutoWired.class)) {
                return constructor;
            }
        }
        if (clazz.getDeclaredConstructors().length == 1) {
            return clazz.getDeclaredConstructors()[0];
        }
        return null;
    }

    /*
    private <T> void injectAnnotatedFields(T object, Field[] declaredFields) throws Exception {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(SimpleAutoWired.class)) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object innerObject = objectRegistryMap.get(type);
                field.set(object, innerObject);
                injectAnnotatedFields(innerObject, type.getDeclaredFields());
            }
        }
    }*/


    /*
    private Set<Class<?>> findClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
