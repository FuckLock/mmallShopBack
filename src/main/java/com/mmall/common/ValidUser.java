package com.mmall.common;

import com.mmall.pojo.User;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpSession;

@Aspect
public class ValidUser {

    @Pointcut("execution(* com.mmall.controller.*.*(..))")
    public void test() {}

    @Before("test()")
    public void validIdentity() {
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大 大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");

        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");
        System.out.println("大大大大大大大大大大大大大大");



//        User user = (User)session.getAttribute(Const.CURRENT_USER);
//        if (user == null) {
//            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员");
//        }
//
//        if(user != null && user.getRole().intValue() != Const.Role.ROLE_ADMIN) {
//            return ServerResponse.createByErrorMessage("无权限操作");
//        }
//        return null;
    }
}
