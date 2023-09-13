package com.ruoyi.busi.aspectj;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.busi.domain.TMemberUser;
import com.ruoyi.busi.service.ITMemberUserService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 某些小程序接口权限，需要登录获取用户信息了之后 才能允许访问，
 */
@Aspect
@Order(-200)
@Component
public class NeedWechatLoginAspect {

    private static final Logger log = LoggerFactory.getLogger(NeedWechatLoginAspect.class);


    public static ThreadLocal<TMemberUser> wechatLoginThreadLocal = new ThreadLocal<TMemberUser>();


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ITMemberUserService memberUserService;


    //@Pointcut注解声明切点
    @Pointcut("@annotation(com.ruoyi.common.annotation.NeedWechatLogin)")
    public void performance() {
    }

    //@Around注解声明环绕通知的方法
    @Around("performance()")
    public Object watchPerformance(ProceedingJoinPoint jointPoint) throws Throwable {
        try {
            log.info("查看是否有小程序登录权限");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();


            String wxTokenStr = request.getHeader("wxToken");//KEY wxToken :  VALUE wxtoken:wvy5uncxie


            if (wxTokenStr == null || "".equals(wxTokenStr)) {
                //直接拦截 响应
                AjaxResult error = AjaxResult.error(401, "未登录 或者 已经失效");
                ServletUtil.write(response, JSONUtil.parse(error).toString(), "application/json;charset=utf-8");


            } else {
                //从redis中读取 之前登录的用户信息
                TMemberUser tMemberUser = redisCache.getCacheObject(wxTokenStr);

                if (tMemberUser == null) {
                    AjaxResult error = AjaxResult.error(401, "未登录 或者 已经失效");
                    ServletUtil.write(response, JSONUtil.parse(error).toString(), "application/json;charset=utf-8");
                } else {
                    //正常走下去请求
                    //查询最新的用户信息
                    Long id = tMemberUser.getId();
                    tMemberUser = memberUserService.selectTMemberUserById(id);

                    //存储到 threadLocal中，在之后 当前线程任意的地方 都可以获取到
                    wechatLoginThreadLocal.set(tMemberUser);

                    //继续调用controller
                    Object proceed = jointPoint.proceed();


                    return proceed;
                }
            }
            return null;
        } finally {
            wechatLoginThreadLocal.remove(); //移除 防止内存泄露
        }

    }


}
