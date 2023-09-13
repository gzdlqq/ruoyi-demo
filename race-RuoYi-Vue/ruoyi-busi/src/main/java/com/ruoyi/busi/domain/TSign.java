package com.ruoyi.busi.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * sign对象 t_sign
 *
 * @author shenzhao
 * @date 2023-04-16
 */
public class TSign extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** member fk外键 */
    @Excel(name = "member fk外键")
    private Long userId;

    /** 选手姓名 冗余 */
    @Excel(name = "选手姓名 冗余")
    private String playerName;

    /** 赛事id  fk */
    @Excel(name = "赛事id  fk")
    private Long competitionId;

    /** 赛事名称 冗余 */
    @Excel(name = "赛事名称 冗余")
    private String competitionName;

    /** 报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报名时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signTime;

    /** 总金额 */
    @Excel(name = "总金额")
    private BigDecimal totalFee;

    /** 业务订单号 全局唯一 一般使用雪花算法 */
    @Excel(name = "业务订单号 全局唯一 一般使用雪花算法")
    private Long tradeNo;

    /** 0未付款 1 已经付款(待审核)  2审核通过（报名成功） 3.订单关闭  4审核拒绝（退款）  */
    @Excel(name = "0未付款 1 已经付款(待审核)  2审核通过（报名成功） 3.订单关闭  4审核拒绝（退款）", readConverterExp = "报名成功")
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public String getPlayerName()
    {
        return playerName;
    }
    public void setCompetitionId(Long competitionId)
    {
        this.competitionId = competitionId;
    }

    public Long getCompetitionId()
    {
        return competitionId;
    }
    public void setCompetitionName(String competitionName)
    {
        this.competitionName = competitionName;
    }

    public String getCompetitionName()
    {
        return competitionName;
    }
    public void setSignTime(Date signTime)
    {
        this.signTime = signTime;
    }

    public Date getSignTime()
    {
        return signTime;
    }
    public void setTotalFee(BigDecimal totalFee)
    {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalFee()
    {
        return totalFee;
    }
    public void setTradeNo(Long tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public Long getTradeNo()
    {
        return tradeNo;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("playerName", getPlayerName())
            .append("competitionId", getCompetitionId())
            .append("competitionName", getCompetitionName())
            .append("signTime", getSignTime())
            .append("totalFee", getTotalFee())
            .append("tradeNo", getTradeNo())
            .append("status", getStatus())
            .toString();
    }
}
