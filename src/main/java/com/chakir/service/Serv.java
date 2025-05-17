package com.chakir.service;


import com.chakir.annotations.SimplyAutoWire;
import com.chakir.entity.Test;
import com.chakir.repository.Rep;

public class Serv {
    @SimplyAutoWire
    private Rep repo;

    public Test test(Integer id){
        return repo.getById(id);
    }
}
