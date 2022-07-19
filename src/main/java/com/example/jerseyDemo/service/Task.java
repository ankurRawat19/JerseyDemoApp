package com.example.jerseyDemo.service;

import java.util.concurrent.TimeUnit;

public class Task  implements Runnable{
    private String name;
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void run() {
        try {
            Long duration = (long) (Math.random() * 10);
            System.out.println("Executing : " + name);
            TimeUnit.SECONDS.sleep(duration);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
