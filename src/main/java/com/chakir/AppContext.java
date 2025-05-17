package com.chakir;

import annotations.ComponentScan;
import annotations.Configuration;
import annotations.SimpleComponent;
import annotations.SimplyAutoWire;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class AppContext {
    Map<Class<?>,Object> objectRegistryMap = new HashMap<>();

    public AppContext(Class<?> clazz){
        initialiseContext(clazz);
    }
    public <T> T getInstance(Class<T> clazz) throws Exception {
        // getting the public default constructor of the class using its getConstructor method
        //Constructor<?> constructor = clazz.getConstructor();
        //creating a new instance of said class
        T object = (T) objectRegistryMap.get(clazz);

        Field[] declaredFields = clazz.getDeclaredFields();
        injectAnnotedFields(object,declaredFields);

        return object;
    }

    private <T> void injectAnnotedFields(T object, Field[] declaredFields) throws Exception {
        for (Field field : declaredFields) {
            if(field.isAnnotationPresent(SimplyAutoWire.class)){
                field.setAccessible(true);//we set this to true incase the field is private
                Class<?> type = field.getType();
                Object innerObject = type.getDeclaredConstructor().newInstance();

                field.set(object,innerObject);
                //we recursively call injectAnnotatedField
                injectAnnotedFields(innerObject,type.getDeclaredFields());
            }
        }
    }
    private void initialiseContext(Class<?> clazz){
        if(!clazz.isAnnotationPresent(Configuration.class)) {
            throw new RuntimeException("Please provide a valid configuration file!");
        }else{
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            String packageValue = componentScan.value();
            Set<Class<?>> classes = findClasses(packageValue);
            for(Class<?> loadingClass:classes){
                try {
                    if(loadingClass.isAnnotationPresent(SimpleComponent.class)){
                        Constructor<?> constructor = loadingClass.getDeclaredConstructor();
                        Object newInstance = constructor.newInstance();
                        objectRegistryMap.put(loadingClass,newInstance);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private Set<Class<?>> findClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]","/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
            + className.substring(0,className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }





}

