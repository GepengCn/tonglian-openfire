package com.itonglian.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomThreadPool {

    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService(){
        return executorService;
    }
}
