package com.nokia.logtools.bo;

/**
 * 查询参数配置bean
 */
public class Page {

    /**
     * 当前页码
     */
    private Integer index;

    /**
     * 每页条数
     */
    private Integer limit;

    /**
     * 升降序方式
     */
    private String order;

    /**
     * 排序列字段
     */
    private String sidx;

    /**
     * 查询总记录数
     */
    private Long count;

    /**
     * 显示当前分页的总页码
     */
	@SuppressWarnings("unused")
	private Integer pageSize=0;

    public Page() {

    }

    public Page(Integer index) {
        this.index = index;
    }

    public Page(Page page) {
        this.index = page.getIndex();
        this.limit = page.getLimit();
        this.order = page.getOrder();
        this.sidx = page.getSidx();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public long getPageSize() {
        return this.getCount() % this.getLimit() == 0 ? this.getCount() / getLimit() : this.getCount() / getLimit() + 1;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
