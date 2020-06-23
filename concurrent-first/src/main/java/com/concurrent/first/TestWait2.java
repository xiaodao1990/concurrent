package com.concurrent.first;

/**
 * 线程执行wait方法后，被唤醒是接着上次执行地方往下执行还是会重新执行临界区的代码？
 *      0 1 2 3 4 5 进入wait状态。。。。。。
 *      唤醒线程。。。。。。
 *      6 7 8 9
 *
 * 通过结果可以看出，进入wait状态的线程被唤醒后，是接着上次执行的地方继续执行。
 */
public class TestWait2 {

    private int a = 0;

    private synchronized void count() {
        for (int i = 0; i < 10; i++) {
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
    }

    public static void main(String[] args) throws InterruptedException {
        final TestWait2 testWait2 = new TestWait2();
        new Thread(new Runnable() {
            public void run() {
                testWait2.count();
            }
        }).start();

        Thread.sleep(2000);
        synchronized (testWait2) {
            System.out.println("唤醒线程。。。。。。");
            testWait2.notifyAll();
        }
    }
}
