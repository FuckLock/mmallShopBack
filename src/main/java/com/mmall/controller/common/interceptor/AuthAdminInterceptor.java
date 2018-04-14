package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 此项目默认使用这种方式，下面做了简单说明
 * springmvc 拦截器方式验证管理员
 * 如果你使用这种方式，你需要做的是
 * 1.springmvc的dispathcer-servlet.xml中配置interceptor的配置即可
 */
public class AuthAdminInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthAdminInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //请求过来先打印log日志
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramsMap = httpServletRequest.getParameterMap();
        Set<Map.Entry<String, Object>> entrySet = paramsMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet){
            String key = entry.getKey();
            Object obj = entry.getValue();

            String mapValue = StringUtils.EMPTY;

            if(obj instanceof String[]){
                String[] value = (String[])obj;
                mapValue = Arrays.toString(value);
            }
            requestParamBuffer.append(key).append("=").append(mapValue);
        }
        logger.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}", className, methodName, requestParamBuffer.toString());

        // 验证管理员
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if(StringUtils.isEmpty(loginToken)){
            Map resultMap = Maps.newHashMap();
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            out.print(JsonUtil.obj2String(resultMap));
            return false;
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
            resultMap.put("msg","请登录管理员");
            out.print(JsonUtil.obj2String(resultMap));
            return false;
        }

        if(user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            Map resultMap = Maps.newHashMap();
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();
            resultMap.put("success",false);
            resultMap.put("msg","不是管理员无权操作");
            out.print(JsonUtil.obj2String(resultMap));
            return false;
        }
        httpServletRequest.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
