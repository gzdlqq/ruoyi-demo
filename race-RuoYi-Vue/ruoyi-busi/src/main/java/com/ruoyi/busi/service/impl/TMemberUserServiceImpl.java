package com.ruoyi.busi.service.impl;

import java.util.List;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.busi.mapper.TMemberUserMapper;
import com.ruoyi.busi.domain.TMemberUser;
import com.ruoyi.busi.service.ITMemberUserService;

/**
 * 用户Service业务层处理
 *
 * @author shenzhao
 * @date 2023-03-30
 */
@Service
public class TMemberUserServiceImpl implements ITMemberUserService
{
    @Autowired
    private TMemberUserMapper tMemberUserMapper;

    @Autowired
    private RedisCache redisCache;
    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    @Override
    public TMemberUser selectTMemberUserById(Long id)
    {
        return tMemberUserMapper.selectTMemberUserById(id);
    }

    /**
     * 查询用户列表
     *
     * @param tMemberUser 用户
     * @return 用户
     */
    @Override
    public List<TMemberUser> selectTMemberUserList(TMemberUser tMemberUser)
    {
        return tMemberUserMapper.selectTMemberUserList(tMemberUser);
    }

    /**
     * 新增用户
     *
     * @param tMemberUser 用户
     * @return 结果
     */
    @Override
    public int insertTMemberUser(TMemberUser tMemberUser)
    {
        tMemberUser.setCreateTime(DateUtils.getNowDate());
        return tMemberUserMapper.insertTMemberUser(tMemberUser);
    }

    /**
     * 修改用户
     *
     * @param tMemberUser 用户
     * @return 结果
     */
    @Override
    public int updateTMemberUser(TMemberUser tMemberUser)
    {
        tMemberUser.setUpdateTime(DateUtils.getNowDate());
        return tMemberUserMapper.updateTMemberUser(tMemberUser);
    }

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteTMemberUserByIds(Long[] ids)
    {
        return tMemberUserMapper.deleteTMemberUserByIds(ids);
    }

    /**
     * 删除用户信息
     *
     * @param id 用户主键
     * @return 结果
     */
    @Override
    public int deleteTMemberUserById(Long id)
    {
        return tMemberUserMapper.deleteTMemberUserById(id);
    }

    @Override
    public TMemberUser registUserAndLogin(String openid, String phoneNumber) {
        //1.首先 根据 openid 去查询 是否已经存在这条记录
        TMemberUser tMemberUser = new TMemberUser();
        tMemberUser.setOpenId(openid);
        //根据openid 查询 用户数量
        List<TMemberUser> tMemberUsers = tMemberUserMapper.selectTMemberUserList(tMemberUser);

        if(tMemberUsers.size() == 0){
            //新用户 需要注册先 ，插入数据
            tMemberUser.setOpenId(openid);
            tMemberUser.setMobile(phoneNumber);
            tMemberUser.setNickname("小瘪三");
            tMemberUser.setAvatar("");
            tMemberUser.setDelFlag("0");
            tMemberUser.setStatus(0L);//正常

            int i = tMemberUserMapper.insertTMemberUser(tMemberUser);
            System.out.println("插入自动生成的主键是:"+tMemberUser.toString());


        }else{
            //老用户了
            tMemberUser = tMemberUsers.get(0);
        }


        return tMemberUser;
    }
}
