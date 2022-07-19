package com.example.jerseyDemo.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolExecutorService {

    public static String callSimpleThreadPoolExecutorService() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setName("task name");
            System.out.println("Created : " + task.getName());

            executor.execute(task);
        }
        executor.shutdown();

        return "tasks created. see console";
    }

}