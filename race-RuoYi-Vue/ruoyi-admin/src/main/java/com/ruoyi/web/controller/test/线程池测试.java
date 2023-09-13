package com.ruoyi.web.controller.test;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class 线程池测试 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(2);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(300);
// 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize(); // 显式地初始化线程池

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程池测试1");
            }
        });

        Future<?> 线程池测试2 = executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程池测试2");
            }
        });
        System.out.println("线程池测试2:"+线程池测试2);
        System.out.println("线程池测试2:"+线程池测试2.get());

        Future<Object> submit = executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "返沪";
            }
        });
        System.out.println("线程池测试3:"+submit);
        System.out.println("线程池测试3:"+submit.get());



        ListenableFuture<?> 线程池测试4 = executor.submitListenable(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程池测试4");
            }
        });
        //在上述代码中，使用 submitListenable() 方法提交了一个带有回调的任务，
        // 并通过 ListenableFuture 对象注册了回调函数。
        // ListenableFutureCallback 接口包含了 onSuccess() 和 onFailure() 方法，分别在任务成功完成和任务执行失败时被调用
        线程池测试4.addCallback(new ListenableFutureCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                System.out.println("任务成功完成");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("任务执行失败: " + ex.getMessage());
            }
        });








    }
}
