package com.concurrent.second;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.TestAQS")
public class TestAQS {

    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(()->{
            lock.lock();
            log.debug(Thread.currentThread().getName()+" locking");
            lock.lock();
            log.debug(Thread.currentThread().getName()+" locking");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug(Thread.currentThread().getName()+" unlock");
                lock.unlock();
            }
        }, "t1").start();

        new Thread(()->{
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName()+" locking");
            } finally {
                log.debug(Thread.currentThread().getName()+" unlock");
                lock.unlock();
            }
        }, "t2").start();
    }
}
