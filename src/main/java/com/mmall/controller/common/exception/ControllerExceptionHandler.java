package com.mmall.controller.common.exception;


import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * 有点操蛋，mockmvc测试没问题，启动服务器没效果，麻蛋，有待研究
 *1.缺点： 只能处理controller报的异常
 * 思考：  那么如何处理其他类的异常呢？？？？
 * 方法：可以定义一个子类，然后自己thorw new exception 来抛出吧。
 */
//@ControllerAdvice
public class ControllerExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleException(Exception e, HttpServletRequest request) {
        logger.error("访问请求{}  \n Exception", request.getRequestURI(), e);
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", ResponseCode.ERROR.getCode());
        result.put("msg","接口异常,详情请查看服务端日志的异常信息。。。。。。。。。。");
        result.put("data", e.toString());
        return result;
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ServerResponse handleException(Exception e, HttpServletRequest request) {
//        logger.error("访问请求{}  \n Exception", request.getRequestURI(), e);
//        ServerResponse response = ServerResponse.createByError("接口异常,详情请查看服务端日志的异常信息", e.toString());
//        return response;
//    }
}
