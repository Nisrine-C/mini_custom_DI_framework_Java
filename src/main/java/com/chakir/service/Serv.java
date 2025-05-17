package com.chakir.service;


import annotations.SimpleComponent;
import annotations.SimpleAutoWired;
import com.chakir.entity.Test;
import com.chakir.repository.Rep;

@SimpleComponent
public class Serv {
    @SimpleAutoWired
    private Rep repo;
    @SimpleAutoWired
    public Serv(Rep repo){
        this.repo = repo;
    }
    public Test test(Integer id){
        return repo.getById(id);
    }
}
