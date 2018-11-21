package com.nokia.logtools.constants;

/**
 * @author 
 * @version 1.00.00
 * </pre>
 */
public enum ErrorCode {

    //错误代码定义规范:

    //通用处理成功代码2xx
    SUCC_DO("200", "处理成功"),
    SUCC_USER_LOGIN("201", "登陆成功"),
    SUCC_USER_SAVE("202", "用户信息保存成功"),
    //通用错误代码4xx
    ERROR_EXPRESSION("401", "表达式验证错误"),
    ERROR_PATAM_NULL("402", "参数为空"),
    ERROR_CODE("403", "处理失败"),
    ERROR_BEAN_COPY_NULL("404", "对象复制不能为null"),
    ERROR_GET_USER_FAIL("405", "获取用户信息失败"),
    ERROR_BEAN_COPY("406", "对象复制异常"),
    ERROR_PHONE_EXISTED("407", "该手机号已经注册过了"),
    ERROR_PHONE_VALIDATE_CODE("408", "手机号验证码错误"),
    ERROR_PHONE_VALIDATE_NULL("409", "没有输入手机号验证码"),
    ERROR_PHONE_VALIDATE_BLANK("410", "手机号验证码为空"),
    ERROR_PHONE_PHONE_BLANK("411", "手机号不能为空"),
    ERROR_TOKEN_EXPIRED("412", "登录超时，请重新登陆"),
    ERROR_TOKEN_NULL("413", "请登陆"),
    ERROR_TOKEN_INVALID("414", "TOKEN验证失败");
	
    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}