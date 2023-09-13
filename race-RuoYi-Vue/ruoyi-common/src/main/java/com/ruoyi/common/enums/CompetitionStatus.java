package com.ruoyi.common.enums;

/**
 * 用户状态
 *
 * @author ruoyi
 */
public enum CompetitionStatus
{
    WEIFABU("0", "未发布"),
    JIJIANGKAISHI("1", "即将开始"),
    BAOMINGZHONG("2", "报名中"),
    BIANPAIZHONG("3", "编排中"),
    BISAIZHONG("4", "比赛中"),
    JIESHU("5", "结束状态")
    ;

    private final String value;
    private final String label;


    CompetitionStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
