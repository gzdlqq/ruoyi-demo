package com.ruoyi.web.controller.test;

import java.util.HashMap;
import java.util.concurrent.*;

public class CompletableFuture测试1 {

        public static void main(String[] args) throws ExecutionException, InterruptedException {

            ExecutorService executorService = Executors.newFixedThreadPool(10);

            UserInfoService userInfoService = new UserInfoService();
            MedalService medalService = new MedalService();
            long userId =666L;
            long startTime = System.currentTimeMillis();

            //调用用户服务获取用户基本信息
            FutureTask<HashMap> userInfoFutureTask = new FutureTask<>(new Callable<HashMap>() {
                @Override
                public HashMap call() throws Exception {
                    HashMap<String, String> userInfo = userInfoService.getUserInfo(userId);
                    return userInfo;
                }
            });
            executorService.submit(userInfoFutureTask);



            FutureTask<HashMap> medalInfoFutureTask = new FutureTask<>(new Callable<HashMap>() {
                @Override
                public HashMap call() throws Exception {
                    return medalService.getMedalInfo(userId);
                }
            });
            executorService.submit(medalInfoFutureTask);



            Future<HashMap> future = executorService.submit(new Callable<HashMap>() {
                @Override
                public HashMap call() throws Exception {
                    HashMap<String, String> medalInfo = medalService.getMedalInfo(userId);
                    return medalInfo;
                }
            });


            Thread.sleep(300); //模拟主线程其它操作耗时

            HashMap userInfo = userInfoFutureTask.get();//获取个人信息结果
            HashMap medalInfo = medalInfoFutureTask.get();//获取勋章信息结果
            HashMap hashMap = (HashMap) future.get();
            System.out.println("用户信息：" + userInfo);
            System.out.println("勋章信息：" + medalInfo);
            System.out.println("勋章信息：" + hashMap);

            System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
        }


}

 class UserInfoService {

    public HashMap<String, String> getUserInfo(Long userId) throws InterruptedException {
        Thread.sleep(300);//模拟调用耗时
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name","李四");
        return hashMap ; //一般是查数据库，或者远程调用返回的
    }
}

 class MedalService {

    public HashMap<String, String> getMedalInfo(long userId) throws InterruptedException {
        Thread.sleep(500); //模拟调用耗时
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("xunzhang","6666杀敌一千");
        return hashMap;
    }
}

