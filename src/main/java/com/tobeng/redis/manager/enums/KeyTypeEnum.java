package com.tobeng.redis.manager.enums;

import lombok.Getter;

/**
 * redis 展示类型
 */
@Getter
public enum KeyTypeEnum {

    /**
     * redis 数据库
     */
    REDIS_DB(1),

    /**
     * redis 文件分页
     */
    REDIS_FILE(2),

    /**
     * redis key
     */
    REDIS_KEY(3);

    private Integer code;

    KeyTypeEnum(Integer code){
        this.code = code;
    }

}
