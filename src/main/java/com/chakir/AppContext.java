package com.chakir;

import java.util.HashMap;
import java.util.Map;

public class AppContext {
    private Map<Class<?>,Object> beans = new HashMap<>();

    public AppContext(String basePackage) {
        //scanComponents(basePackage);
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    private void scanComponents(String basePackage) {

    }
}

