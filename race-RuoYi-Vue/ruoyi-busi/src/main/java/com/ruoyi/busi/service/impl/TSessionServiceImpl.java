package com.ruoyi.busi.service.impl;

import java.util.*;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.busi.domain.*;
import com.ruoyi.busi.mapper.*;
import com.ruoyi.busi.utils.BusiUtils;
import com.ruoyi.common.exception.ServiceException;
import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.busi.service.ITSessionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 场次Service业务层处理
 *
 * @author shenzhao
 * @date 2023-05-07
 */
@Service
@Slf4j
public class TSessionServiceImpl implements ITSessionService
{
    @Autowired
    private TSessionMapper tSessionMapper;

    @Autowired
    private TSessionDetailMapper tSessionDetailMapper;
    @Autowired
    private TCompetitionMapper competitionMapper;

    @Autowired
    private TRaceSchemaMapper raceSchemaMapper;

    @Autowired
    private TSignMapper signMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询场次
     *
     * @param id 场次主键
     * @return 场次
     */
    @Override
    public TSession selectTSessionById(Long id)
    {
        return tSessionMapper.selectTSessionById(id);
    }

    /**
     * 查询场次列表
     *
     * @param tSession 场次
     * @return 场次
     */
    @Override
    public List<TSession> selectTSessionList(TSession tSession)
    {
        return tSessionMapper.selectTSessionList(tSession);
    }

    /**
     * 新增场次
     *
     * @param tSession 场次
     * @return 结果
     */
    @Override
    public int insertTSession(TSession tSession)
    {
        return tSessionMapper.insertTSession(tSession);
    }

    /**
     * 修改场次
     *
     * @param tSession 场次
     * @return 结果
     */
    @Override
    public int updateTSession(TSession tSession)
    {
        return tSessionMapper.updateTSession(tSession);
    }

    /**
     * 批量删除场次
     *
     * @param ids 需要删除的场次主键
     * @return 结果
     */
    @Override
    public int deleteTSessionByIds(Long[] ids)
    {
        return tSessionMapper.deleteTSessionByIds(ids);
    }

    /**
     * 删除场次信息
     *
     * @param id 场次主键
     * @return 结果
     */
    @Override
    public int deleteTSessionById(Long id)
    {
        return tSessionMapper.deleteTSessionById(id);
    }

    @Transactional
    @Override
    public int arrange(Long competitionId) {

        //先清空之前编排的场次
        tSessionMapper.deleteTSessionByCompetitionId(competitionId);
        //场次详情也清空下
        tSessionDetailMapper.deleteTSessionDetailByCompetitionId(competitionId);

        //查询当前赛事
        TCompetition tCompetition = competitionMapper.selectTCompetitionById(competitionId);


        //如果当前时间 小于 报名结束时间，就不允许编排
        if(System.currentTimeMillis()<tCompetition.getRegistrationEndTime().getTime()){
            throw new ServiceException("当前赛事未到达编排阶段，请稍后再编排");
        }
        //查询赛事方案
        TRaceSchema tRaceSchema = raceSchemaMapper.selectTRaceSchemaById(tCompetition.getRaceSchemaId());
        //判断赛制
        String format = tRaceSchema.getFormat();
        String ruleId = "";
        String url = "http://127.0.0.1:9224/bianpai/addRuleConfig";
        String body1= null;
        //1 单循环赛 2 淘汰赛
        if("1".equals(format)){
            //调用保存单循环赛事方案接口，拿到第三方的赛事方案id
            //保存循环赛的赛事方案接口
            body1="{\n" +
                    "\n" +
                    "  \"formatType\": 1,\n" + //单循环赛
                    "  \"knockoutAssignRuleId\": 1,\n" +
                    "  \"singleCycleBasePosition\": 1,\n" +
                    "  \"singleCycleFirstRoundAssignRuleId\": 1,\n" +
                    "  \"singleCycleMoveDirection\": 1,\n" +
                    "  \"singleCycleMovePosition\": 1\n" +
                    "}";

        }else if("2".equals(format)) {
            //调用保存淘汰赛事方案接口，拿到第三方的赛事方案id
            //保存淘汰赛的赛事方案接口
            body1="{\n" +
                    "\"formatType\": 3,\n" +
                    "\"knockoutAssignRuleId\": 1\n" +
                    "}";


        }

        //post请求，请求格式是 json ，通过hutool的HttpUtil工具类
        String result1 = null;
        try {
            result1 = HttpUtil.post(url, body1);
        } catch (Exception e) {
            throw new ServiceException("调用第三方接口失败");
        }
//        System.out.println(result1);
        //解析出data.ruleId属性
        JSON parse = JSONUtil.parse(result1);
        //第三方编排系统的  赛事方案id 已经拿到
        ruleId = (String) parse.getByPath("data.ruleId");





        //查询当前赛事已经报名审核通过的所有选手
        List<TSign> list = signMapper.selectTSignListByCompetitionId(competitionId);

        if(list.size()<2){
            throw new ServiceException("当前赛事报名人数不足，无法编排");
        }

        /**
         {
         "piciName": "哈哈哈哈",
         "signPlayers": [
             {
             "thirdPartId": 88,
             "name": "寒冰射手",
             "baseSeed": 0,
             "ability": 1,
             "additionalParameter": ""
             },
             {
             "thirdPartId": 99,
             "name": "法外狂徒",
             "baseSeed": 0,
             "ability": 10,
             "additionalParameter": ""
             },
             {
             "thirdPartId": 199,
             "name": "披甲龙龟",
             "baseSeed": 0,
             "ability": 5,
             "additionalParameter": ""
             }
         ],
         "ruleId": 1660559548365742080
         }
         */
        //封装signPlayers参数
        List<Map<String,Object>> signPlayers = new ArrayList<>();
        for (TSign tSign : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("thirdPartId",tSign.getId());
            map.put("name",tSign.getPlayerName());
            signPlayers.add(map);
        }

        //封装请求参数
        Map<String,Object> param = new HashMap<>();
        param.put("piciName",tCompetition.getName());
        param.put("signPlayers",signPlayers);
        param.put("ruleId",ruleId);

        //调用编排接口
        String body = JSONUtil.toJsonStr(param);
        String result = null;
        try {
            result = HttpUtil.post("http://localhost:9224/bianpai/bianpaiForThirdPartV2", body);
        } catch (Exception e) {
            throw new ServiceException("调用第三方接口失败");
        }

        /**
         * 解析结果
         */
        JSONObject resultJson = JSONUtil.parseObj(result);
        Integer code = resultJson.getInt("code");
        if(code!=200){
            throw new ServiceException("调用第三方接口失败");
        }
        JSONObject data = resultJson.getJSONObject("data");
//        Long piciId = data.getLong("piciId");
        JSONArray groupList = data.getJSONArray("groupList");
        //取第一个分组
        JSONObject group = groupList.getJSONObject(0);
        /**
         sessionsList:[
           {
         "id": 1233,
         "groupId": 13340,
         "sessionsNo": 1,
         "roundNo": 1,
         "roundInNo": 1,
         "sessionsDetailList": [
                 {
                 "leftOrRight": 1,
                 "faker": "0",
                 "bindParticipantsId": 88,
                 "player": {
                 "thirdPartId": 88,
                 "name": "托塔李天王",
                 "baseSeed": 0,
                 "ability": 0,
                 "additionalParameter": ""
                 },
                 "nextLocationNo": 1,
                 "locationNo": 1,
                 "bindParticipantsMockNo": 1,
                 "id": 2465,
                 "sessionsId": 1233,
                 "groupId": 13340,
                 "sessionsNo": 1,
                 "roundNo": 1,
                 "roundInNo": 1
                 }
                ]
           }
         ]
         */
        JSONArray sessionsJsonArray = group.getJSONArray("sessionsList");
        //转化成TSession对象
        List<TSession> tSessionList = new ArrayList<>();
        for (int i = 0; i < sessionsJsonArray.size(); i++) {
            //拿到遍历的第三方的session元素
            JSONObject sessionsJson = sessionsJsonArray.getJSONObject(i);
            TSession tSession = new TSession();
            tSession.setId(sessionsJson.getLong("id"));
            tSession.setSessionNo(sessionsJson.getLong("sessionsNo"));
            tSession.setRoundNo(sessionsJson.getLong("roundNo"));
            tSession.setRoundSessionNo( sessionsJson.getLong("roundInNo"));
            tSession.setCompetitionId(competitionId);

            tSession.setBaseGameOver(0L);
            tSessionList.add(tSession);

            JSONArray sessionsDetailList = sessionsJson.getJSONArray("sessionsDetailList");
            // 转化成TSessionDetail对象
            List<TSessionDetail> tSessionDetailList = new ArrayList<>();
            for (int j = 0; j < sessionsDetailList.size(); j++) {
                JSONObject sessionsDetailJson = sessionsDetailList.getJSONObject(j);
                TSessionDetail tSessionDetail = new TSessionDetail();
                tSessionDetail.setId( sessionsDetailJson.getLong("id"));
                tSessionDetail.setSessionId( sessionsDetailJson.getLong("sessionsId"));
                tSessionDetail.setSessionNo( sessionsDetailJson.getLong("sessionsNo"));
                tSessionDetail.setRoundNo( sessionsDetailJson.getLong("roundNo"));
                tSessionDetail.setRoundSessionNo( sessionsDetailJson.getLong("roundInNo"));
                tSessionDetail.setLeftOrRight( sessionsDetailJson.getLong("leftOrRight"));
                tSessionDetail.setFaker( sessionsDetailJson.getLong("faker"));
                tSessionDetail.setNextRoundLocationNo( sessionsDetailJson.getLong("nextLocationNo"));
                tSessionDetail.setLocationNo( sessionsDetailJson.getLong("locationNo"));
                tSessionDetail.setCompetitionId( tCompetition.getId());


                tSessionDetail.setBaseGameOver(0L);

                if(sessionsDetailJson.getLong("faker")==0L){
                    //"player": {
                    //                 "thirdPartId": 88,
                    //                 "name": "托塔李天王"
                    //                 },
                    JSONObject playerJson = sessionsDetailJson.getJSONObject("player");
                    Long thirdPartId = playerJson.getLong("thirdPartId");//传过去的报名主键
                    String name = playerJson.getStr("name");//选手名称
                    tSessionDetail.setNickname(name);
                    tSessionDetail.setSignId(thirdPartId);
                    //根据报名id，去查询userId
                    TSign tSign = signMapper.selectTSignById(thirdPartId);
                    Long userId = tSign.getUserId();
                    tSessionDetail.setUserId(userId);


                }

                tSessionDetailList.add(tSessionDetail);
            }
            tSession.setSessionsDetailList(tSessionDetailList);
        }

        //12*11=132  132/2=66
        System.out.println(tSessionList);


        //保存场次信息
        tSessionMapper.saveBatch(tSessionList);



        //1.批量保存场次明细信息  效率差的做法
//        for (TSession tSession : tSessionList) {
//            List<TSessionDetail> sessionsDetailList = tSession.getSessionsDetailList();
//            tSessionDetailMapper.saveBatch(sessionsDetailList);
//        }

        //2.效率高的做法
        List<TSessionDetail> tSessionDetailList = new ArrayList<>();
        for (TSession tSession : tSessionList) {
            List<TSessionDetail> sessionsDetailList = tSession.getSessionsDetailList();
            tSessionDetailList.addAll(sessionsDetailList);
        }
        tSessionDetailMapper.saveBatch(tSessionDetailList);


        /**
         * 发送消息给mq
         */
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("tSessionList",tSessionList);
        stringObjectHashMap.put("tSessionDetailList",tSessionDetailList);
        rabbitTemplate.convertAndSend("esExchange","bianpaiRoutingKey",stringObjectHashMap);








        return 0;
    }

    @Override
    public HashMap xunhuanSessions(Long competitionId) {
        HashMap<String, Object> map = new HashMap<>();


        /**
         * 这里有三种查询方式
         *  1.sql 联查 查询一次，所有记录都出来了，推荐，但是麻烦没写  推荐*
         *  2.先查询sessionList，然后循环 每次循环都查询sessionDetailList，效率低  非要这么 并行流 和 多线程用起来*
         *  3.先查询sessionList，再查询所有detail，然后手动绑定关系，利用java的引用传递，效率高  也推荐，两次查询  效率也高
         *
         */
        //根据比赛id查询出所有的场次 和 场次详情
        List<TSession> tSessionList = tSessionMapper.selectTSessionByCompetitionId(competitionId);
        if(tSessionList.size()==0){
            throw new ServiceException("暂无场次信息，请编排之后查询");
        }
        //查询场次详情
        List<TSessionDetail> tSessionDetailList = tSessionDetailMapper.selectTSessionDetailByCompetitionId(competitionId);

        //绑定关系  内存级别的操作
        for (TSession tSession : tSessionList) {
            List<TSessionDetail> tSessionDetails = new ArrayList<>();
            for (TSessionDetail tSessionDetail : tSessionDetailList) {
                if(tSession.getId().equals(tSessionDetail.getSessionId())){
                    tSessionDetails.add(tSessionDetail);
                }
                //如果size已经=2，直接 下一次循环
                if(tSessionDetails.size()==2){
                    break;
                }
            }
            tSession.setSessionsDetailList(tSessionDetails);
        }


        //查询最大的轮次
        Long roundNo = tSessionList.stream().max(Comparator.comparing(TSession::getRoundNo)).get().getRoundNo();
        System.out.println(roundNo);
        //查询最大的轮内场次
        Long roundInNo = tSessionList.stream().max(Comparator.comparing(TSession::getRoundSessionNo)).get().getRoundSessionNo();
        System.out.println(roundInNo);

        map.put("row",roundInNo); //行 就是 最大轮
        map.put("col",roundNo); //列 就是一轮 最大第几场
        map.put("list",tSessionList); //场次信息

        return map;
    }


}
