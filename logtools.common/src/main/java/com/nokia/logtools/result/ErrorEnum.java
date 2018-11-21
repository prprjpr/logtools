package com.nokia.logtools.result;

/**
 * explain:
 *
 * @author: LiMeng
 * <p>
 * CreateDate 2018/5/26.
 */
public enum ErrorEnum {

    //系统内部错误
    SYSTEM_ERROR(400000, "Internal error of the system"),
    //授权失败
    AUTHORIZATION_ERROR(400001, "Authorization Error"),
    //api不存在
    API_NO_EXISTENT(400002, "Api no existed"),
    //请求方式错误
    METHOD_NOT_SUPPORTE(400003,"Method Not Supporte"),
    //请求参数体错误
    REQUEST_PARAMETER_ERROR(400004,"Request Parameter Error"),
    //参数为空
    PARAMETER_IS_EMTPY(400005,"Parameter is empty"),
    //保存信息时错误
    SAVEA_ERROR(400006,"Save Error"),
    //更新错误
    UPDATE_ERROR(400007,"Update Error");


    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;


    public static String getMsg(Integer index) {
        for (ErrorEnum c : ErrorEnum.values()) {
            if (c.getCode() == index) {
                return c.msg;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
