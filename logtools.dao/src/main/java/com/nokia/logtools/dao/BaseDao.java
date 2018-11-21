package com.nokia.logtools.dao;


import java.util.List;


/**
 * explain:
 * @author: 
 * <p>
 * CreateDate 
 */
public interface BaseDao<T> {

    /**
     * 添加一个实体
     */
    int insert(T t);

    /**
     * gen一个实体
     */
    int update(T t);

    /**
     * 更新一个实体
     */

    int updateSpecify(T t);

    /**
     * 根据实体ID，删除实体
     */
    int deleteById(Integer id);

    /**
     * 根据实体ID，查找实体
     */

    T getById(Integer id);

    /**
     * 查询符合查询参数的实体结果集数量
     */
    int findResultCount(T t);

    /**
     * 查询符合查询参数的实体结果集
     */
    List<T> findResults(T t);

    /**
     * 查询所有数据
     * @return
     */
    List<T> findAll();

}
