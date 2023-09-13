package com.ruoyi.busi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.busi.mapper.TSessionDetailMapper;
import com.ruoyi.busi.domain.TSessionDetail;
import com.ruoyi.busi.service.ITSessionDetailService;

/**
 * 场次详情Service业务层处理
 * 
 * @author shenzhao
 * @date 2023-05-07
 */
@Service
public class TSessionDetailServiceImpl implements ITSessionDetailService 
{
    @Autowired
    private TSessionDetailMapper tSessionDetailMapper;

    /**
     * 查询场次详情
     * 
     * @param id 场次详情主键
     * @return 场次详情
     */
    @Override
    public TSessionDetail selectTSessionDetailById(Long id)
    {
        return tSessionDetailMapper.selectTSessionDetailById(id);
    }

    /**
     * 查询场次详情列表
     * 
     * @param tSessionDetail 场次详情
     * @return 场次详情
     */
    @Override
    public List<TSessionDetail> selectTSessionDetailList(TSessionDetail tSessionDetail)
    {
        return tSessionDetailMapper.selectTSessionDetailList(tSessionDetail);
    }

    /**
     * 新增场次详情
     * 
     * @param tSessionDetail 场次详情
     * @return 结果
     */
    @Override
    public int insertTSessionDetail(TSessionDetail tSessionDetail)
    {
        return tSessionDetailMapper.insertTSessionDetail(tSessionDetail);
    }

    /**
     * 修改场次详情
     * 
     * @param tSessionDetail 场次详情
     * @return 结果
     */
    @Override
    public int updateTSessionDetail(TSessionDetail tSessionDetail)
    {
        return tSessionDetailMapper.updateTSessionDetail(tSessionDetail);
    }

    /**
     * 批量删除场次详情
     * 
     * @param ids 需要删除的场次详情主键
     * @return 结果
     */
    @Override
    public int deleteTSessionDetailByIds(Long[] ids)
    {
        return tSessionDetailMapper.deleteTSessionDetailByIds(ids);
    }

    /**
     * 删除场次详情信息
     * 
     * @param id 场次详情主键
     * @return 结果
     */
    @Override
    public int deleteTSessionDetailById(Long id)
    {
        return tSessionDetailMapper.deleteTSessionDetailById(id);
    }
}
