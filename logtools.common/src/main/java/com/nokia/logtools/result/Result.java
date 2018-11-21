package com.nokia.logtools.result;

import java.io.Serializable;

/**
 * 
 * @author yehao
 *
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 2290594340707133291L;

	/**
	 * 返回成功
	 * 
	 * @param msg
	 * @param data
	 * @return
	 */
	public static final Result success(String msg, Object data) {
		Result r = new Result();
		r.setIsSuccess(true);
		r.setMsg(msg);
		r.setData(data);
		r.setCode(0);
		return r;
	}

	public static final Result success(Object data) {
		return success("ok", data);
	}

	/**
	 * 错误是调用
	 * 
	 * @param errcode
	 *            错误码
	 * @param msg
	 *            消息体
	 * @return
	 */
	public static final Result error(Integer errcode, String msg) {
		Result r = new Result();
		r.setIsSuccess(false);
		r.setMsg(msg);
		r.setData(null);
		r.setCode(errcode);
		return r;
	}

	/**
	 * 默认传递系统内部错误
	 * 
	 * @param msg
	 * @return
	 */
	public static final Result error(String msg) {
		return error(ErrorEnum.SYSTEM_ERROR.getCode(), msg);
	}

	/**
	 * 是否成功
	 */
	private boolean isSuccess;

	/**
	 * 消息
	 */
	private String msg;

	/**
	 * 错误代码
	 */
	private Integer code;

	/**
	 * 数据
	 */
	private Object data;

	public boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * 是否成功
	 * 
	 * @param result
	 *            true 或 false
	 */
	public void setIsSuccess(boolean result) {
		isSuccess = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Result{" + "isSuccess=" + isSuccess + ", msg='" + msg + '\'' + ", code='" + code + '\'' + ", data="
				+ data + '}';
	}
}
