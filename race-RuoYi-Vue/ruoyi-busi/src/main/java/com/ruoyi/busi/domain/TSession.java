package com.ruoyi.busi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

import java.util.List;

/**
 * 场次对象 t_session
 *
 * @author shenzhao
 * @date 2023-05-07
 */
public class TSession extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;

    /** 赛事id FK外键 */
    @Excel(name = "赛事id FK外键")
    private Long competitionId;

    /** 总共场次第几场 */
    @Excel(name = "总共场次第几场")
    private Long sessionNo;

    /** 第几轮 */
    @Excel(name = "第几轮")
    private Long roundNo;

    /** 轮内第几场 */
    @Excel(name = "轮内第几场")
    private Long roundSessionNo;

    /** 场次是否结束 1结束 0未结束 */
    @Excel(name = "场次是否结束 1结束 0未结束")
    private Long baseGameOver;

    //场次详情 集合
    @IndexField(fieldType = FieldType.NESTED, nestedClass = TSessionDetail.class)
    private List<TSessionDetail> sessionsDetailList;


    public List<TSessionDetail> getSessionsDetailList() {
        return sessionsDetailList;
    }

    public void setSessionsDetailList(List<TSessionDetail> sessionsDetailList) {
        this.sessionsDetailList = sessionsDetailList;
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
    public void setBaseGameOver(Long baseGameOver)
    {
        this.baseGameOver = baseGameOver;
    }

    public Long getBaseGameOver()
    {
        return baseGameOver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("competitionId", getCompetitionId())
            .append("sessionNo", getSessionNo())
            .append("roundNo", getRoundNo())
            .append("roundSessionNo", getRoundSessionNo())
            .append("baseGameOver", getBaseGameOver())
            .toString();
    }
}
