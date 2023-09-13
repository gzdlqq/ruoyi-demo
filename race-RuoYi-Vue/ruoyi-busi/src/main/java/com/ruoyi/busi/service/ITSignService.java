package com.ruoyi.busi.service;

import java.util.List;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.domain.resp.MySign;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * signService接口
 *
 * @author shenzhao
 * @date 2023-04-16
 */
public interface ITSignService
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
     * 批量删除sign
     *
     * @param ids 需要删除的sign主键集合
     * @return 结果
     */
    public int deleteTSignByIds(Long[] ids);

    /**
     * 删除sign信息
     *
     * @param id sign主键
     * @return 结果
     */
    public int deleteTSignById(Long id);

    Object sign(Long competitionId);

    void paySuccess(Long tradeNo);

    List<MySign> queryMySign();

    Object continueToPay(String tradeNo);

    void reject(Long signId) throws WxPayException, WxErrorException;

    void access(Long signId) throws WxErrorException;
}
