package com.ruoyi.busi.mapper;

import java.util.List;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.domain.resp.MySign;

/**
 * signMapper接口
 *
 * @author shenzhao
 * @date 2023-04-16
 */
public interface TSignMapper
{
    /**
     * 查询sign
     *
     * @param id sign主键
     * @return sign
     */
    public TSign selectTSignById(Long id);

    /**
     * 查询sign列表
     *
     * @param tSign sign
     * @return sign集合
     */
    public List<TSign> selectTSignList(TSign tSign);

    /**
     * 新增sign
     *
     * @param tSign sign
     * @return 结果
     */
    public int insertTSign(TSign tSign);

    /**
     * 修改sign
     *
     * @param tSign sign
     * @return 结果
     */
    public int updateTSign(TSign tSign);

    /**
     * 删除sign
     *
     * @param id sign主键
     * @return 结果
     */
    public int deleteTSignById(Long id);

    /**
     * 批量删除sign
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSignByIds(Long[] ids);

    List<MySign> queryMySign(Long userId);

    TSign selectTSignByTradeNo(String tradeNo);

    List<TSign> selectTSignListByCompetitionId(Long competitionId);
}
