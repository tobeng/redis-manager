package com.tobeng.redis.manager.service;

import java.util.List;

/**
 * redis 基本信息 service
 *
 * @author yr
 * @date 2019/09/13
 * @since 1.0.0
 */
public interface RedisService {

    /**
     * 获取 redis key 树形结构
     * @return key 树形结构
     */
    Object getKeyTree();

    /**
     * 获取 key 的值
     *
     * @param key redis 缓存 key
     * @return 信息
     */
    Object getDataByKey(String key);

    /**
     * 获取模糊匹配 key
     *
     * @param key
     * @return
     */
    List<Object> getMatchKey(String key);

    /**
     * 删除缓存 key
     *
     * @param key 缓存 key
     */
    void deleteDataByKey(String key);
}
