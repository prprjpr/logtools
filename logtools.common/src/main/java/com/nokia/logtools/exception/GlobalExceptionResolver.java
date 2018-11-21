package com.nokia.logtools.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.alibaba.fastjson.JSONObject;
import com.nokia.logtools.result.ErrorEnum;
import com.nokia.logtools.result.Result;
import com.nokia.logtools.utils.LogUtils;

/**
 * explain: 全局异常处理类
 *
 * @author: 
 * <p>
 * CreateDate 
 */
@ControllerAdvice
public class GlobalExceptionResolver extends DefaultHandlerExceptionResolver {


    @ExceptionHandler(value = Exception.class)
    public ModelAndView DefaultResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getServletPath();
        LogUtils.error("全局异常: 客户端请求路径 " + url + " 时发生异常\n");
        ex.printStackTrace();
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            setResponseParam(response, ErrorEnum.METHOD_NOT_SUPPORTE.getCode(), ErrorEnum.METHOD_NOT_SUPPORTE.getMsg());
            return null;
        }

        if (ex instanceof MissingServletRequestParameterException) {
            setResponseParam(response, ErrorEnum.REQUEST_PARAMETER_ERROR.getCode(), ErrorEnum.REQUEST_PARAMETER_ERROR.getMsg());
            return null;
        }

        if (ex instanceof NoHandlerFoundException) {
            //可以进行其他方法处理，LOG或者什么详细记录，我这里直接返回JSON
            setResponseParam(response, ErrorEnum.API_NO_EXISTENT.getCode(), ErrorEnum.API_NO_EXISTENT.getMsg());
            return null;
        }
        //自定义异常
        if (ex instanceof BizException) {
            setResponseParam(response, ((BizException) ex).getResult().getCode(), ((BizException) ex).getResult().getMsg());
            return null;
        }
        setResponseParam(response, ErrorEnum.SYSTEM_ERROR.getCode(), ErrorEnum.SYSTEM_ERROR.getMsg());
        return null;
        //这里调用父类的异常处理方法，实现其他不需要的异常交给SpringMVC处理
    }

    private void setResponseParam(HttpServletResponse response, int code, String msg) {
        String resultJson = null;
        resultJson = JSONObject.toJSONString(Result.error(code, msg));
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().print(resultJson);
        } catch (IOException e) {
            LogUtils.error("全局异常: 将异常信息写入客户端时错误");
            e.printStackTrace();
        }
    }
}
