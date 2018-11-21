package com.nokia.logtools.exception;



import com.nokia.logtools.result.ErrorEnum;
import com.nokia.logtools.result.Result;

/**
 * explain: 全局业务异常定义
 *
 * @author:
 * <p>
 * CreateDate 
 */
public class BizException extends RuntimeException  {

    private static final long serialVersionUID = -714695329212910322L;

    private Result result = new Result();

    /**
     *
     * @param errcode 传入 ErrorEnum枚举中的错误代码
     * @param errmsg
     */
    public BizException(Integer errcode, String errmsg) {
        super(errmsg);
        result.setCode(errcode);
        result.setMsg(errmsg);
    }

    public BizException(String errmsg) {
        super(errmsg);
        result.setCode(ErrorEnum.SYSTEM_ERROR.getCode());
        result.setMsg(errmsg);
    }

    private BizException() {

    }

    public Result getResult() {
        return result;
    }
}
