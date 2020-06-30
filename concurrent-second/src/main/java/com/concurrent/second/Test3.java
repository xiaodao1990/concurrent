package com.concurrent.second;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Test3")
public class Test3 {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        new Thread(()->{
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName()+" locking");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();
        new Thread(()->{
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName()+" locking");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
