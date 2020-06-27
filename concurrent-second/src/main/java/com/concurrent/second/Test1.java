package com.concurrent.second;

import lombok.extern.slf4j.Slf4j;



@Slf4j(topic="c.Test1")
public class Test1 {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug("running");
            }
        });
        thread.start();

        Runnable task = () -> {
            log.debug("lamda runing...");
        };
        Thread thread1 = new Thread(task);
        thread1.start();

        System.out.println();


    }
}
