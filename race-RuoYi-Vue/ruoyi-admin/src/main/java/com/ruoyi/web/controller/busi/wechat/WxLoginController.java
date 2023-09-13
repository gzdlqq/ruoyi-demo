package com.ruoyi.web.controller.busi.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.util.RandomUtil;
import com.ruoyi.busi.domain.TMemberUser;
import com.ruoyi.busi.domain.resp.AppAuthLoginRespVO;
import com.ruoyi.busi.domain.vo.AppAuthWeixinMiniAppLoginReqVO;
import com.ruoyi.busi.service.ITMemberUserService;
import com.ruoyi.common.annotation.NeedWechatLogin;
import com.ruoyi.common.config.WxMaProperties;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.busi.aspectj.NeedWechatLoginAspect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * 微信小程序登录
 */
@Api(tags = "移动C端-微信小程序登录")
@RestController
@RequestMapping("/wechat")
public class WxLoginController {


    @Autowired
    WxMaService wxMaService;

    @Autowired
    WxMaProperties wxMaProperties;


    @Autowired
    ITMemberUserService itMemberUserService;

    @Autowired
    RedisCache redisCache;



    @PostMapping("/weixin-mini-app-login")
    @ApiOperation("微信小程序的一键登录")
    public AjaxResult weixinMiniAppLogin(@RequestBody @Valid AppAuthWeixinMiniAppLoginReqVO reqVO, HttpServletRequest request) throws WxErrorException {


        if (StringUtils.isBlank(reqVO.getLoginCode())) {
            return AjaxResult.error(500,"登陆码不能为空");
        }

        try {
            //根据登录零时码  换取 获取 openid
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(reqVO.getLoginCode());
            String openid = session.getOpenid();
            System.out.println("换取的openid是:"+openid);

            //根据 零时手机码  换取 获取手机号
            WxMaPhoneNumberInfo newPhoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(reqVO.getPhoneCode());
            String phoneNumber = newPhoneNoInfo.getPhoneNumber();
            System.out.println("换取到的手机号:"+phoneNumber);


            //全部换取成功 应该 注册 和 登录
            TMemberUser user = itMemberUserService.registUserAndLogin(openid,phoneNumber);

            //存储token到redis
            String wxtoken = "wxtoken:" + RandomUtil.randomString(10);
            redisCache.setCacheObject(wxtoken,user,600, TimeUnit.MINUTES);




            //响应 token给前端
            AppAuthLoginRespVO appAuthLoginRespVO = new AppAuthLoginRespVO();
            appAuthLoginRespVO.setUserId(user.getId());
            appAuthLoginRespVO.setWxToken(wxtoken);


            return AjaxResult.success("登录成功",appAuthLoginRespVO);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw e;
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }

    }


    @GetMapping("/getInfo")
    @ApiOperation("获取 用户信息接口")
    @NeedWechatLogin //代表需要登录 才能访问
    public AjaxResult getInfo( HttpServletRequest request) throws WxErrorException {

//        String wxToken = request.getHeader("wxToken");//header这个key是什么？
//        TMemberUser cacheObject = redisCache.getCacheObject(wxToken);
//        System.out.println(cacheObject);
//        //如果考虑到 用户可能会更新这种情况，要么在用户更新的时候 就要去判断下缓存中是否存储了用户信息，如果存储了 就是重新覆盖
//        //或者说，直接 在这里 拿到id查询最新的用户信息
//        Long id = cacheObject.getId();
//        TMemberUser tMemberUser = itMemberUserService.selectTMemberUserById(id);

        TMemberUser tMemberUser = NeedWechatLoginAspect.wechatLoginThreadLocal.get();

        return AjaxResult.success(tMemberUser);
    }



}
