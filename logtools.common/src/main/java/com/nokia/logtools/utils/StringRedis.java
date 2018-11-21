//package com.ycqh.zongzigame.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * explain:
// *
// * @author: LiMeng
// * <p>
// * CreateDate 2018/6/9.
// */
//@Component("stringRedis")
//public class StringRedis {
//
//
//    @Autowired
//    private ValueOperations<String, Object> redisTemplate;
//
//    /**
//     * set:(保存数据).
//     * @author Joe
//     * Date:2017年11月14日下午8:15:01
//     * @param key
//     * @param value
//     */
//    public void set(String key, String value){
//        redisTemplate.set(key, value);
//    }
//
//    /**
//     *
//     * @param key
//     * @param value
//     * @param timeout 过期时间
//     */
//    public void set(String key, String value, Integer timeout) {
//        redisTemplate.set(key, value, timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     * get:(得到数据).
//     * @author Joe
//     * Date:2017年11月14日下午8:15:38
//     * @param key
//     * @return
//     */
//    public Object get(String key) {
//        return redisTemplate.get(key);
//    }
//}
