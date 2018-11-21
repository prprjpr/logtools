package com.nokia.logtools.constants;

/**
 * 结果类型
 *
 * @author tangjunfeng
 */
public enum ResultType {

    FAILURE(0), // 执行失败（适用于调用流程操作）

    SUCCESS(1), // 执行成功（适用于调用流程操作）

    HADATA(2), // 有数据（适用于查询操作）

    NODATA(3), // 无数据（适用于查询操作）

    EXCEPTION(4);// 异常

    private int val;

    ResultType(int val) {
        this.val = val;
    }

    public int val() {
        return this.val;
    }

}
