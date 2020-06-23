package com.concurrent.first;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyLock {

    public static void main(String[] args) throws InterruptedException {
        final Account from = new Account(10000);
        final Account to = new Account(10000);
        final CountDownLatch countDownLatch = new CountDownLatch(10000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    from.transfer(to, 1);
                    countDownLatch.countDown();
                }
            }).start();
        }
        for (int i = 0; i < 5000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    to.transfer(from, 1);
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("from="+from.getBalance() + ", to="+to.getBalance() + ", time="+(System.currentTimeMillis()-start));
    }

    /**
     * 账户类
     */
    static class Account {

        private Allocator actr = Allocator.getInstance();
        private int balance;

        public Account(int balance) {
            this.balance = balance;
        }

        public int getBalance() {
            return balance;
        }

        // 转账
        public void transfer(Account target, int amt) {
            // 次性申请转出账户和转入账户，直到成功
            actr.apply(this, target);
            try {
                // 锁定转出账户
                synchronized (this) {
                    // 锁定转入账户
                    synchronized (target) {
                        if (this.balance > amt) {
                            this.balance -= amt;
                            target.balance += amt;
                        }
                    }
                }
            } finally {
                actr.free(this, target);
            }
        }

    }

    /**
     * 账本管理类
     */
    static class Allocator {

        public static Allocator INSTANCE = new Allocator();
        private List<Account> locks = new ArrayList<Account>();

        private Allocator() {
        }

        public static Allocator getInstance() {
            return INSTANCE;
        }

        // 一次申请所有的资源
        public synchronized void apply(Account from, Account to) {
            while (locks.contains(from) || locks.contains(to)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            locks.add(from);
            locks.add(to);
        }

        // 归还资源
        public synchronized void free(Account from, Account to) {
            locks.remove(from);
            locks.remove(to);
            notifyAll();
        }
    }
}


