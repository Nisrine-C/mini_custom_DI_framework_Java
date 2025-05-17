package com.chakir.repository;

import annotations.SimpleComponent;
import com.chakir.entity.Test;

import java.util.HashMap;
import java.util.Map;

@SimpleComponent
public class Rep {
    Map<Integer, Test> testIdToTestMap = new HashMap<>();

    public Rep(){
        testIdToTestMap.put(1,new Test(1,"test1"));
        testIdToTestMap.put(2,new Test(2,"test2"));
        testIdToTestMap.put(3,new Test(3,"test3"));
        testIdToTestMap.put(4,new Test(4,"test4"));
    }

    public Test getById(Integer id) {
        return testIdToTestMap.get(id);
    }
}
