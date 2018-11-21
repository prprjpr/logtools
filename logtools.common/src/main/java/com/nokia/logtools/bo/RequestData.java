package com.nokia.logtools.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 * @author 
 */
public class RequestData<T> implements Serializable {

    private static final long serialVersionUID = 4555779266019511486L;

    //请求头
    private Map<String, Object> headerMap = new HashMap<>();

    //其他参数 如文件之类
    private Map<String, Object> parmas = new HashMap<>();

    // 分页参数
    private Page page;

    // 请求参数
    private T data;

    public RequestData() {
    }

    public RequestData(T data) {
        this.data = data;
    }


    public Object getHeaderMap(String key) {
        return headerMap.get(key);
    }

    public void setHeaderMap(String key, Object value) {
        this.headerMap.put(key, value);
    }

    public Map<String, Object> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, Object> headerMap) {
        this.headerMap = headerMap;
    }


    public Object getParmas(String key) {
        return parmas.get(key);
    }

    public void setParmas(String key, Object value) {
        this.parmas.put(key, value);
    }

    public Map<String, Object> getParmas() {
        return parmas;
    }

    public void setParmas(Map<String, Object> parmas) {
        this.parmas = parmas;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

}
