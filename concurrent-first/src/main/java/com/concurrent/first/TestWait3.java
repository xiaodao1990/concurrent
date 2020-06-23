package com.concurrent.first;

/**
 * 被唤醒的线程是否还持有原来的锁？
 *
 */
public class TestWait3 {

    private int a = 0;

    private synchronized void count() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.print(i+" ");
            if (i == 5) {
                try {
                    System.out.println("进入wait状态。。。。。。");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("count执行完毕，释放this锁资源");
    }

    public static void main(String[] args) throws InterruptedException {
        final TestWait3 testWait = new TestWait3();
        new Thread(new Runnable() {
            public void run() {
                testWait.count();
            }
        }).start();

        Thread.sleep(2000);
        synchronized (testWait) {
            System.out.println("唤醒线程。。。。。。");
            testWait.notifyAll();
        }
        Thread.sleep(1000);
        System.out.println("main线程尝试再次获取testWait锁");
        synchronized (testWait) {
            System.out.println("main线程获取testWait锁成功");
        }
    }
}
