package com.concurrent.first;

/**
 * 解惑：wait方法是否会释放同步块中所持有的锁？
 *      线程1拿到lock1锁
 *      线程1准备获取lock2锁
 *      线程1拿到lock2锁
 *      线程1准备释放lock1锁
 *      线程2拿到了lock1锁，开始运行
 *      线程2准备获取lock2锁
 *
 * 从运行结果可以看出，线程1调用了lock1.wait()后，释放了lock1锁，所以线程2成功拿到了lock1锁，开始了运行。
 */
public class TestWait1 {
    // 对象锁1
    private static Object lock1 = new Object();
    // 对象锁2
    private static Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                synchronized (lock1) {
                    System.out.println("线程1拿到lock1锁");
                    System.out.println("线程1准备获取lock2锁");
                    synchronized (lock2) {
                        System.out.println("线程1拿到lock2锁");
                        try {
                            System.out.println("线程1准备释放lock1锁");
                            lock1.wait();
                            System.out.println("线程-运行结束");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    // 睡眠1s，让线程能够成功运行到wait方法，释放lock1锁
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println("线程2拿到了lock1锁，开始运行");
                    System.out.println("线程2准备获取lock2锁");
                    synchronized (lock2) {
                        System.out.println("线程2拿到了lock2锁，开始运行");
                        System.out.println("线程2运行结束");
                    }
                }
            }
        });
        thread1.start();
        thread2.start();
    }
}
