package com.chakir.service;


import annotations.SimpleComponent;
import annotations.SimplyAutoWire;
import com.chakir.entity.Test;
import com.chakir.repository.Rep;

@SimpleComponent
public class Serv {
    @SimplyAutoWire
    private Rep repo;

    public Test test(Integer id){
        return repo.getById(id);
    }
}
