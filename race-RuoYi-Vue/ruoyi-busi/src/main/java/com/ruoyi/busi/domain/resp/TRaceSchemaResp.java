package com.ruoyi.busi.domain.resp;

import com.ruoyi.busi.domain.TRaceSchema;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 赛事方案对象 t_race_schema
 *
 * @author shenzhao
 * @date 2023-04-05
 */
public class TRaceSchemaResp extends TRaceSchema
{
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
