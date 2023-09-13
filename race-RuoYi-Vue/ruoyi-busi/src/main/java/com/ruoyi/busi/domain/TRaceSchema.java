package com.ruoyi.busi.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 赛事方案对象 t_race_schema
 *
 * @author shenzhao
 * @date 2023-04-05
 */
public class TRaceSchema extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 方案名称 */
    @Excel(name = "方案名称")
    private String name;

    /** 1 循环赛 2 淘汰赛 */
    @Excel(name = "1 单循环赛 2 淘汰赛")
    private String format;

    /** 收费价格 */
    @Excel(name = "收费价格")
    private BigDecimal price;

    //第三方的赛事方案id

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
    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getFormat()
    {
        return format;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("format", getFormat())
            .append("price", getPrice())
            .toString();
    }
}
