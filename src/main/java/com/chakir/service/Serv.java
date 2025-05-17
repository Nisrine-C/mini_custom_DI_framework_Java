package com.chakir.service;

import com.chakir.annotations.Component;
import com.chakir.annotations.Inject;
import com.chakir.entity.Test;
import com.chakir.repository.Rep;

@Component
public class Serv {
    @Inject
    private Rep repo;

    public Serv() {
        repo = new Rep();
    }
    public Test test(Integer id){
        return repo.getById(id);
    }
}
