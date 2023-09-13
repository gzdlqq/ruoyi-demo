package com.ruoyi.web.controller.test;

import java.util.HashMap;
import java.util.concurrent.*;

public class CompletableFuture测试2 {

        public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

            UserInfoService userInfoService = new UserInfoService();
            MedalService medalService = new MedalService();
            long userId =666L;
            long startTime = System.currentTimeMillis();

            //调用用户服务获取用户基本信息
            CompletableFuture<HashMap> completableUserInfoFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    int i = 1 / 0;
                    return userInfoService.getUserInfo(userId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            Thread.sleep(300); //模拟主线程其它操作耗时

            CompletableFuture<HashMap> completableMedalInfoFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return medalService.getMedalInfo(userId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            try {
                HashMap userInfo = completableUserInfoFuture.get(2,TimeUnit.SECONDS);//获取个人信息结果
            } catch (Exception e) {
//                e.printStackTrace();
                throw e;
            }
            HashMap medalInfo = completableMedalInfoFuture.get();//获取勋章信息结果
            System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");


        }


}


