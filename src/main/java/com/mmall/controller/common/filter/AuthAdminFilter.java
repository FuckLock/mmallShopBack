package com.mmall.controller.common.filter;


import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * spring filter方式验证管理员
 * 如果您使用下面方式您需要做的如下
 * 1.打开springmvc的dispathcer-servlet.xml中的注释filter部分的代码即可
 */
public class AuthAdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            Map resultMap = Maps.newHashMap();
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            resultMap.put("success",false);
            resultMap.put("msg","filter验证请登录管理员");
            out.print(JsonUtil.obj2String(resultMap));
            out.close();
            return;
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            Map resultMap = Maps.newHashMap();
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            resultMap.put("success",false);
            resultMap.put("msg","filter验证请登录管理员");
            out.print(JsonUtil.obj2String(resultMap));
            out.close();
            return;
        }

        if(user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            Map resultMap = Maps.newHashMap();
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();
            resultMap.put("success",false);
            resultMap.put("msg","filter验证不是管理员无权操作");
            out.print(JsonUtil.obj2String(resultMap));
            return;
        }

        httpServletRequest.setAttribute("user", user);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
