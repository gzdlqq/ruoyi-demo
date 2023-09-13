package com.ruoyi.web.controller.test;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

public class 测试保存赛制方案接口 {
    public static void main(String[] args) {

        //保存循环赛的赛事方案接口
        String url = "http://127.0.0.1:9224/bianpai/addRuleConfig";

        String body="{\n" +
                "\n" +
                "  \"formatType\": 1,\n" +
                "  \"knockoutAssignRuleId\": 1,\n" +
                "  \"singleCycleBasePosition\": 1,\n" +
                "  \"singleCycleFirstRoundAssignRuleId\": 1,\n" +
                "  \"singleCycleMoveDirection\": 1,\n" +
                "  \"singleCycleMovePosition\": 1\n" +
                "}";

        //post请求，请求格式是 json ，通过hutool的HttpUtil工具类
        String result = cn.hutool.http.HttpUtil.post(url, body);

        //如果需要token，一般情况还需要header中拼接令牌参数

        //解析响应值
        /**
         {
         "msg": "保存成功",
         "code": 200,
         "data": {
         "ruleId": "1659239237015912448",
         "ruleConfig": {
         "id": 1659239237015912448,
         "formatType": 1,
         "singleCycleFirstRoundAssignRuleId": 1,
         "singleCycleMovePosition": 1,
         "singleCycleBasePosition": 1,
         "singleCycleMoveDirection": 1,
         "doubleCycleFirstRoundAssignRuleId": null,
         "doubleCycleMovePosition": null,
         "doubleCycleBasePosition": null,
         "doubleCycleMoveDirection": null,
         "knockoutAssignRuleId": 1,
         "groupCycleGroupingRuleId": null,
         "groupCycleGroupSize": null,
         "groupCycleGroupName": null,
         "groupCycleFirstRoundAssignRuleId": null,
         "groupCycleMovePosition": null,
         "groupCycleBasePosition": null,
         "groupCycleMoveDirection": null
         }
         }
         }
         */
        System.out.println(result);
        //解析出data.ruleId属性
        JSON parse = JSONUtil.parse(result);
        String ruleId = (String) parse.getByPath("data.ruleId");
        System.out.println("本次保存返回的规则id是:"+ruleId);

        //1659240469134655488

    }
}
