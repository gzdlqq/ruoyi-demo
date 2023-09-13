package com.ruoyi.busi.mapper;

import java.util.List;
import com.ruoyi.busi.domain.TSessionDetail;

/**
 * 场次详情Mapper接口
 * 
 * @author shenzhao
 * @date 2023-05-07
 */
public interface TSessionDetailMapper 
{
    /**
     * 查询场次详情
     * 
     * @param id 场次详情主键
     * @return 场次详情
     */
    public TSessionDetail selectTSessionDetailById(Long id);

    /**
     * 查询场次详情列表
     * 
     * @param tSessionDetail 场次详情
     * @return 场次详情集合
     */
    public List<TSessionDetail> selectTSessionDetailList(TSessionDetail tSessionDetail);

    /**
     * 新增场次详情
     * 
     * @param tSessionDetail 场次详情
     * @return 结果
     */
    public int insertTSessionDetail(TSessionDetail tSessionDetail);

    /**
     * 修改场次详情
     * 
     * @param tSessionDetail 场次详情
     * @return 结果
     */
    public int updateTSessionDetail(TSessionDetail tSessionDetail);

    /**
     * 删除场次详情
     * 
     * @param id 场次详情主键
     * @return 结果
     */
    public int deleteTSessionDetailById(Long id);

    /**
     * 批量删除场次详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSessionDetailByIds(Long[] ids);

    void saveBatch(List<TSessionDetail> sessionsDetailList);

    List<TSessionDetail> selectTSessionDetailByCompetitionId(Long competitionId);

    void deleteTSessionDetailByCompetitionId(Long competitionId);
}
