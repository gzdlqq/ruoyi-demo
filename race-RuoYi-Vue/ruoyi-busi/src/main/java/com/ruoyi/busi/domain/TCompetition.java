package com.ruoyi.busi.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.enums.CompetitionStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 赛事对象 t_competition
 *
 * @author shenzhao
 * @date 2023-04-03
 */
public class TCompetition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 赛事 ID */
    private Long id;


    @Excel(name = "赛事方案外键")
    private Long raceSchemaId;

    /** 赛事名称 */
    @Excel(name = "赛事名称")
    private String name;

    /** 报名开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date registrationStartTime;

    /** 报名结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date registrationEndTime;

    /** 赛事 banner 图片 URL */
    @Excel(name = "赛事 banner 图片 URL")
    private String bannerUrl;

    /** 比赛开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "比赛开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 预计比赛结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计比赛结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date estimatedEndTime;

    /** 赛事是否结束 */
    @Excel(name = "赛事是否结束")
    private String baseEnd;

    /** 赛事介绍 */
    private String description;

    /** 赛事地点经度 */
    @Excel(name = "赛事地点经度")
    private BigDecimal longitude;

    /** 赛事地点纬度 */
    @Excel(name = "赛事地点纬度")
    private BigDecimal latitude;

    /** 是否发布 */
    @Excel(name = "是否发布 1发布  0 未发布")
    private String basePublished;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 删除标志 */
    @Excel(name = "删除标志")
    private String delFlag;


        @Excel(name = "是否编排 1已经编排 0未编排 ")
    private String baseBianpai;


    /**
     * 这是一个虚拟字段 是根据其他字段 实时计算出来的！
     */
//    @Excel(name = "状态 1.即将开始  2.报名中  3.编排中  4.比赛中  5.结束状态 ")
    private  String status;

    //虚拟字段  权重值
    private  Long quanzhong;

    public Long getQuanzhong() {
        return quanzhong;
    }

    public void setQuanzhong(Long quanzhong) {
        this.quanzhong = quanzhong;
    }

    public String getStatus() {
        return status;
    }

    public String getBaseBianpai() {
        return baseBianpai;
    }

    public void setBaseBianpai(String baseBianpai) {
        this.baseBianpai = baseBianpai;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 绑定的赛事方案实体类对象
     */
    private TRaceSchema raceSchema;

    public TRaceSchema getRaceSchema() {
        return raceSchema;
    }

    public void setRaceSchema(TRaceSchema raceSchema) {
        this.raceSchema = raceSchema;
    }

    public Long getRaceSchemaId() {
        return raceSchemaId;
    }

    public void setRaceSchemaId(Long raceSchemaId) {
        this.raceSchemaId = raceSchemaId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setRegistrationStartTime(Date registrationStartTime)
    {
        this.registrationStartTime = registrationStartTime;
    }

    public Date getRegistrationStartTime()
    {
        return registrationStartTime;
    }
    public void setRegistrationEndTime(Date registrationEndTime)
    {
        this.registrationEndTime = registrationEndTime;
    }

    public Date getRegistrationEndTime()
    {
        return registrationEndTime;
    }
    public void setBannerUrl(String bannerUrl)
    {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerUrl()
    {
        return bannerUrl;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEstimatedEndTime(Date estimatedEndTime)
    {
        this.estimatedEndTime = estimatedEndTime;
    }

    public Date getEstimatedEndTime()
    {
        return estimatedEndTime;
    }
    public void setBaseEnd(String baseEnd)
    {
        this.baseEnd = baseEnd;
    }

    public String getBaseEnd()
    {
        return baseEnd;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude()
    {
        return longitude;
    }
    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }
    public void setBasePublished(String basePublished)
    {
        this.basePublished = basePublished;
    }

    public String getBasePublished()
    {
        return basePublished;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("registrationStartTime", getRegistrationStartTime())
            .append("registrationEndTime", getRegistrationEndTime())
            .append("bannerUrl", getBannerUrl())
            .append("startTime", getStartTime())
            .append("estimatedEndTime", getEstimatedEndTime())
            .append("baseEnd", getBaseEnd())
            .append("description", getDescription())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("basePublished", getBasePublished())
            .append("sort", getSort())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
