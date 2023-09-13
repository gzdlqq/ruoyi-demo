package com.ruoyi.busi.mapper;

import java.util.List;
import com.ruoyi.busi.domain.TMemberUser;

/**
 * 用户Mapper接口
 * 
 * @author shenzhao
 * @date 2023-03-30
 */
public interface TMemberUserMapper 
{
    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    public TMemberUser selectTMemberUserById(Long id);

    /**
     * 查询用户列表
     * 
     * @param tMemberUser 用户
     * @return 用户集合
     */
    public List<TMemberUser> selectTMemberUserList(TMemberUser tMemberUser);

    /**
     * 新增用户
     * 
     * @param tMemberUser 用户
     * @return 结果
     */
    public int insertTMemberUser(TMemberUser tMemberUser);

    /**
     * 修改用户
     * 
     * @param tMemberUser 用户
     * @return 结果
     */
    public int updateTMemberUser(TMemberUser tMemberUser);

    /**
     * 删除用户
     * 
     * @param id 用户主键
     * @return 结果
     */
    public int deleteTMemberUserById(Long id);

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMemberUserByIds(Long[] ids);
}
