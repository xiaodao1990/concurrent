package com.concurrent.second;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j(topic="c.Test2")
public class Test2 {

    public static void main(String[] args) {
        demo(new Supplier<int[]>() {
            @Override
            public int[] get() {
                return new int[10];
            }
        }, new Function<int[], Integer>() {
            @Override
            public Integer apply(int[] array) {
                return array.length;
            }
        }, new BiConsumer<int[], Integer>() {
            @Override
            public void accept(int[] array, Integer index) {
                array[index]++;
            }
        }, new Consumer<int[]>() {
            @Override
            public void accept(int[] array) {
                System.out.println(Arrays.toString(array));
            }
        });
    }

    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer) {
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            ts.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        putConsumer.accept(array, j % length);
                    }
                }
            }));
        }

        for (Thread t : ts) {
            t.start();
        }

        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }// 等所有线程结束
        printConsumer.accept(array);

    }
}
