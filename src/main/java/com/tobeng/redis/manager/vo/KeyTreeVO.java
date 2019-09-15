package com.tobeng.redis.manager.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * redis key 树形结构
 *
 * @author yr
 * @date 2019/09/14
 * @since 1.0.0
 */
@Data
public class KeyTreeVO {

    /**
     * 类型：1、redis数据库，2、文件夹，3、redis 缓存key
     */
    private Integer type;

    private String name;

    private Integer total;

    private List<KeyTreeVO> children;

    public KeyTreeVO(){
        this.children = new ArrayList<>();
        this.total = 1;
    }

    public KeyTreeVO(String name, Integer type){
        this.name = name;
        this.type = type;
        this.children = new ArrayList<>();
        this.total = 1;
    }

    public void totalAutoIncrement(){
        this.total++;
    }

}
