package com.concurrent.first;

public class Final {

    private static final Object xxxLock = new Object();
    private final Object xLock = new Object();

    public static Object getXxxLock() {
        return xxxLock;
    }

    public Object getxLock() {
        return xLock;
    }
}
