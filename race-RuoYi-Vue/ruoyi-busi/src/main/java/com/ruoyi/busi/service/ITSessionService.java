package com.ruoyi.busi.service;

import java.util.HashMap;
import java.util.List;
import com.ruoyi.busi.domain.TSession;

/**
 * 场次Service接口
 *
 * @author shenzhao
 * @date 2023-05-07
 */
public interface ITSessionService
{
    /**
     * 查询场次
     *
     * @param id 场次主键
     * @return 场次
     */
    public TSession selectTSessionById(Long id);

    /**
     * 查询场次列表
     *
     * @param tSession 场次
     * @return 场次集合
     */
    public List<TSession> selectTSessionList(TSession tSession);

    /**
     * 新增场次
     *
     * @param tSession 场次
     * @return 结果
     */
    public int insertTSession(TSession tSession);

    /**
     * 修改场次
     *
     * @param tSession 场次
     * @return 结果
     */
    public int updateTSession(TSession tSession);

    /**
     * 批量删除场次
     *
     * @param ids 需要删除的场次主键集合
     * @return 结果
     */
    public int deleteTSessionByIds(Long[] ids);

    /**
     * 删除场次信息
     *
     * @param id 场次主键
     * @return 结果
     */
    public int deleteTSessionById(Long id);

    int arrange(Long competitionId);

    HashMap xunhuanSessions(Long competitionId);
}
