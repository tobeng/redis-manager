package com.tobeng.redis.manager.enums;

import lombok.Getter;

/**
 * @author yr
 * @date 2019/09/15
 * @since 1.0.0
 */
@Getter
public enum  DataTypeEnum {

    /**
     * 未知
     */
    NONE("none"),

    /**
     * 字符串
     */
    STRING("string"),

    /**
     * list 集合
     */
    LIST("list"),

    /**
     * set 集合
     */
    SET("set"),

    /**
     * set 有序集合
     */
    ZSET("zset"),

    /**
     * hash 集合
     */
    HASH("hash");

    private String code;

    DataTypeEnum(String code){
        this.code = code;
    }
}
