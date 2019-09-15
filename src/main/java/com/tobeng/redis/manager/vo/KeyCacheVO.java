package com.tobeng.redis.manager.vo;

import lombok.Data;

/**
 * @author yr
 * @date 2019/09/15
 * @since 1.0.0
 */
@Data
public class KeyCacheVO {

    private String key;

    private String dateType;

    private Long expire;

    private Integer total;

    public KeyCacheVO(){
        this.total = 1;
    }

}
