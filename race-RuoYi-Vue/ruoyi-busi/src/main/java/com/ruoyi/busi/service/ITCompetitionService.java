package com.ruoyi.busi.service;

import java.util.List;
import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.common.core.domain.PageResp;
import com.ruoyi.common.core.domain.PageVo;

/**
 * 赛事Service接口
 *
 * @author shenzhao
 * @date 2023-04-03
 */
public interface ITCompetitionService
{
    /**
     * 查询赛事
     *
     * @param id 赛事主键
     * @return 赛事
     */
    public TCompetition selectTCompetitionById(Long id);

    /**
     * 查询赛事列表
     *
     * @param tCompetition 赛事
     * @return 赛事集合
     */
    public List<TCompetition> selectTCompetitionList(TCompetition tCompetition);

    /**
     * 新增赛事
     *
     * @param tCompetition 赛事
     * @return 结果
     */
    public int insertTCompetition(TCompetition tCompetition);

    /**
     * 修改赛事
     *
     * @param tCompetition 赛事
     * @return 结果
     */
    public int updateTCompetition(TCompetition tCompetition);

    /**
     * 批量删除赛事
     *
     * @param ids 需要删除的赛事主键集合
     * @return 结果
     */
    public int deleteTCompetitionByIds(Long[] ids);

    /**
     * 删除赛事信息
     *
     * @param id 赛事主键
     * @return 结果
     */
    public int deleteTCompetitionById(Long id);

    void publish(Long id);

    PageResp pageRace(PageVo pageVo, TCompetition competition);
}
