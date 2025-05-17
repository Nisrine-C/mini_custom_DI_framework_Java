package com.chakir;

import com.chakir.annotations.SimplyAutoWire;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AppContext {
    public <T> T getInstance(Class<T> clazz) throws Exception {
        // getting the public default constructor of the class using its getConstructor method
        Constructor<?> constructor = clazz.getConstructor();
        //creating a new instance of said class
        T object = (T) constructor.newInstance();

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
}

