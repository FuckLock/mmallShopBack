package com.mmall.common;

import com.mmall.pojo.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
public class ValidUser {

    // 判断是否是管理员用户
    @Around("execution(* com.mmall.controller.CategoryManageController.*(..)) " +
            "or execution(* com.mmall.controller.ProductController.*(..)) " +
            "or execution(* com.mmall.controller.ProductManageController.*(..)) " +
            "or execution(* com.mmall.controller.OrderManageController.*(..))" )
    public Object validIdentity(ProceedingJoinPoint joinPoint) throws Throwable {
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

    //判断用户是否登录
    @Around("execution(* com.mmall.controller.CartController.*(..)) " +
            "or execution(* com.mmall.controller.ShippingController.*(..))")
    public Object validLogin(ProceedingJoinPoint joinPoint) throws Throwable {
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
}
