package com.chakir;


import com.chakir.service.Serv;

public class Main {
    public static void main(String[] args) throws Exception {
        AppContext appContext = new AppContext(AppConfig.class);
        Serv serv = appContext.getInstance(Serv.class);
        System.out.println(serv.test(2));
    }

}