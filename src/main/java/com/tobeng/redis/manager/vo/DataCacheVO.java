package com.tobeng.redis.manager.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yr
 * @date 2019/09/14
 * @since 1.0.0
 */
@Data
public class DataCacheVO {

    private String dateType;

    private String cacheString;

    private Long expire;

    private List<Object> cacheList;

    private Set<Object> cacheSet;

    private Set<Object> cacheZSet;

    private Map<Object, Object> cacheMap;

    private Object cacheNone;

}
