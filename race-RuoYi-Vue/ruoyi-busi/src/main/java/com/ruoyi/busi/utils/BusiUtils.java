package com.ruoyi.busi.utils;

import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.common.enums.CompetitionStatus;

import java.util.Date;

public class BusiUtils {

    public static void setCompetitionStatus(TCompetition competition){
        if("0".equals(competition.getBasePublished() )){
            //未发布
            competition.setStatus(CompetitionStatus.WEIFABU.getValue());
        }
        Date now = new Date();
        //1.即将开始报名  发布了 当前时间 小于 报名开始时间
        if (now.getTime()<competition.getRegistrationStartTime().getTime()){
            competition.setStatus("1");
        }
        //2.报名中  当前时间 大于 报名开始时间  且 小于报名结束时间
        if (now.getTime()>competition.getRegistrationStartTime().getTime() && now.getTime()<=competition.getRegistrationEndTime().getTime()){
            competition.setStatus("2");
        }

        //3.编排中  当前时间 大于 报名结束时间，且还没有编排  或者  编排了但是还没到 比赛开始时间的
        boolean b = now.getTime() > competition.getRegistrationEndTime().getTime();
        boolean b1 = "0".equals(competition.getBaseBianpai());
        boolean b2 = now.getTime() < competition.getStartTime().getTime();
        if (
                now.getTime()>competition.getRegistrationEndTime().getTime() &&
                (  "0".equals(competition.getBaseBianpai())
                        ||
                        now.getTime()<competition.getStartTime().getTime())
        ){
            competition.setStatus("3");
        }

        //4.比赛中  已经编排， 且 超出比赛开始时间  且赛事未结束的
        if ("1".equals(competition.getBaseBianpai())&&now.getTime()>competition.getStartTime().getTime()&&("0".equals(competition.getBaseEnd())||competition.getBaseEnd()==null)){
            competition.setStatus("4");
        }

        //5.结束的
        if ("1".equals(competition.getBaseEnd())){
            competition.setStatus("5");
        }
    }
}
