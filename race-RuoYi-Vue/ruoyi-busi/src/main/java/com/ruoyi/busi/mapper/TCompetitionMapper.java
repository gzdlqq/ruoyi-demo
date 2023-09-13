package com.ruoyi.busi.mapper;

import java.util.List;
import com.ruoyi.busi.domain.TCompetition;

/**
 * 赛事Mapper接口
 * 
 * @author shenzhao
 * @date 2023-04-03
 */
public interface TCompetitionMapper 
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
     * 删除赛事
     * 
     * @param id 赛事主键
     * @return 结果
     */
    public int deleteTCompetitionById(Long id);

    /**
     * 批量删除赛事
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCompetitionByIds(Long[] ids);
}
