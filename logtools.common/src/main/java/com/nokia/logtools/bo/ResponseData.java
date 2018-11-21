package com.nokia.logtools.bo;

import java.io.Serializable;

public class ResponseData implements Serializable {

    private static final long serialVersionUID = -7576926819530318554L;

    private Boolean isSuccess = true;

    private String code = "200";

    private String message;

    private Page page;

    private Object data;

    public ResponseData() {
        super();
        code = "200";
    }

    public ResponseData(Boolean isSuccess, Object data) {
        super();
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public ResponseData(Boolean isSuccess, String code, String message) {
        super();
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    public ResponseData(Boolean isSuccess, Object data, String message) {
        super();
        this.isSuccess = isSuccess;
        this.data = data;
        this.message = message;
    }

    public ResponseData(Boolean isSuccess, String code, String message, Object data) {
        super();
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseData(Boolean isSuccess, String code, String message, Object data, Page page) {
        super();
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
        this.page = page;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseData success(String message) {
        this.setCode("200");
        this.setIsSuccess(true);
        this.setMessage(message);
        return this;
    }

    public ResponseData failure(String message) {
        this.setCode("500");
        this.setIsSuccess(false);
        this.setMessage(message);
        return this;
    }

}
