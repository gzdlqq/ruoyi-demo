package com.ruoyi.busi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.rely.IdType;

/**
 * 场次详情对象 t_session_detail
 *
 * @author shenzhao
 * @date 2023-05-07
 */
public class TSessionDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;

    /** 赛事id FK外键  冗余 */
    @Excel(name = "赛事id FK外键  冗余")
    private Long competitionId;

    /** 总共场次第几场  冗余 */
    @Excel(name = "场次id  fk")
    private Long sessionId;

    /** 总共场次第几场  冗余 */
    @Excel(name = "总共场次第几场  冗余")
    private Long sessionNo;

    /** 第几轮  冗余 */
    @Excel(name = "第几轮  冗余")
    private Long roundNo;

    /** 轮内第几场  冗余 */
    @Excel(name = "轮内第几场  冗余")
    private Long roundSessionNo;

    /** 轮内位置号 */
    @Excel(name = "轮内位置号")
    private Long locationNo;

    /** 下一轮的位置号 */
    @Excel(name = "下一轮的位置号")
    private Long nextRoundLocationNo;

    /** 报名id */
    @Excel(name = "报名id")
    private Long signId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 用户姓名，冗余 */
    @Excel(name = "用户姓名，冗余")
    private String nickname;

    /** 场次是否结束 1结束 0未结束  冗余 */
    @Excel(name = "场次是否结束 1结束 0未结束  冗余")
    private Long baseGameOver;

    /** 1获胜 2失败 */
    @Excel(name = "1获胜 2失败")
    private Long result;

    //1左 2右
    @Excel(name = "1左 2右")
    private Long leftOrRight;

    //1代表假人  其他则代表真人
    @Excel(name = "1假人 0真人")
    private Long faker;

    public Long getLeftOrRight() {
        return leftOrRight;
    }

    public void setLeftOrRight(Long leftOrRight) {
        this.leftOrRight = leftOrRight;
    }

    public Long getFaker() {
        return faker;
    }

    public void setFaker(Long faker) {
        this.faker = faker;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCompetitionId(Long competitionId)
    {
        this.competitionId = competitionId;
    }

    public Long getCompetitionId()
    {
        return competitionId;
    }
    public void setSessionNo(Long sessionNo)
    {
        this.sessionNo = sessionNo;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getSessionNo()
    {
        return sessionNo;
    }
    public void setRoundNo(Long roundNo)
    {
        this.roundNo = roundNo;
    }

    public Long getRoundNo()
    {
        return roundNo;
    }
    public void setRoundSessionNo(Long roundSessionNo)
    {
        this.roundSessionNo = roundSessionNo;
    }

    public Long getRoundSessionNo()
    {
        return roundSessionNo;
    }
    public void setLocationNo(Long locationNo)
    {
        this.locationNo = locationNo;
    }

    public Long getLocationNo()
    {
        return locationNo;
    }
    public void setNextRoundLocationNo(Long nextRoundLocationNo)
    {
        this.nextRoundLocationNo = nextRoundLocationNo;
    }

    public Long getNextRoundLocationNo()
    {
        return nextRoundLocationNo;
    }
    public void setSignId(Long signId)
    {
        this.signId = signId;
    }

    public Long getSignId()
    {
        return signId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getNickname()
    {
        return nickname;
    }
    public void setBaseGameOver(Long baseGameOver)
    {
        this.baseGameOver = baseGameOver;
    }

    public Long getBaseGameOver()
    {
        return baseGameOver;
    }
    public void setResult(Long result)
    {
        this.result = result;
    }

    public Long getResult()
    {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("competitionId", getCompetitionId())
            .append("sessionNo", getSessionNo())
            .append("roundNo", getRoundNo())
            .append("roundSessionNo", getRoundSessionNo())
            .append("locationNo", getLocationNo())
            .append("nextRoundLocationNo", getNextRoundLocationNo())
            .append("signId", getSignId())
            .append("userId", getUserId())
            .append("nickname", getNickname())
            .append("baseGameOver", getBaseGameOver())
            .append("result", getResult())
            .toString();
    }
}
