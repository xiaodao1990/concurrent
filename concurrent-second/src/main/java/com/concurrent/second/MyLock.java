package com.concurrent.second;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j(topic = "c.MyLock")
public class MyLock implements Lock {

    private MySync sync = new MySync();

    /**
     * 尝试加锁，不成功则进入等待队列，不可打断
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 尝试加锁，不成功则进入等待队列，可打断
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 尝试一次，不成功则返回，不进入队列
     * @return
     */
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    /**
     * 尝试，不成功，进入等待队列，有时限
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        sync.release(0);
    }

    /**
     * 生成条件变量
     * @return
     */
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
