package com.mmall.common;

import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;


@Aspect
public class ValidUser {

    // 判断用户是否登录, 此方法是采用cookie和redis完成登录验证
    @Around("execution(* com.mmall.controller.CartController.*(..)) " +
            "or execution(* com.mmall.controller.ShippingController.*(..))" +
            "or execution(* com.mmall.controller.OrderController.*(..))" )
    public Object validCookieLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        Object[] args = joinPoint.getArgs();
        args[0] = user;
        return joinPoint.proceed(args);
    }

    // 判断是否是管理员用户, 此方法是采用cookie和redis完成管理员验证
    @Around("execution(* com.mmall.controller.CategoryManageController.*(..)) " +
            "or execution(* com.mmall.controller.ProductController.*(..)) " +
            "or execution(* com.mmall.controller.ProductManageController.*(..)) " +
            "or execution(* com.mmall.controller.OrderManageController.*(..))" )
    public Object validCookieIdentity(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        if(user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("不是管理员无权限操作");
        }
        return joinPoint.proceed();
    }

    /*
    判断是否是管理员用户, 采用session方式，已经废弃
    @Around("execution(* com.mmall.controller.CategoryManageController.*(..)) " +
            "or execution(* com.mmall.controller.ProductController.*(..)) " +
            "or execution(* com.mmall.controller.ProductManageController.*(..)) " +
            "or execution(* com.mmall.controller.OrderManageController.*(..))" )
    public Object validIdentity(ProceedingJoinPoint joinPoint, HttpServletRequest httpServletRequest) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
        }else if (user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("无权限操作");
        }else {
            return joinPoint.proceed();
        }
    }


    判断用户是否登录，采用session方式，已经废弃
    @Around("execution(* com.mmall.controller.CartController.*(..)) " +
            "or execution(* com.mmall.controller.ShippingController.*(..))")
    public Object validLogin(ProceedingJoinPoint joinPoint, HttpServletRequest httpServletRequest) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else {
            Object[] args = joinPoint.getArgs();
            args[0] = user;
            return joinPoint.proceed(args);
        }
    }
    */


}
