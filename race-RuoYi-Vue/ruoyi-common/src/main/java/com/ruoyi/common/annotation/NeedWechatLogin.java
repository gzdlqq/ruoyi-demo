package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * 需要微信登录的注解
 *      在需要登录微信用户的接口方法上，加上这个注解
 * @author ruoyi
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedWechatLogin
{

}
