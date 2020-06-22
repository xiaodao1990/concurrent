package com.concurrent.first;

public class Test {

    public static void main(String[] args) {
        Final f1 = new Final();
        Final f2 = new Final();
        System.out.println(f1.getxLock().equals(f2.getxLock()));
    }
}
