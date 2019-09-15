package com.tobeng.redis.manager.controller;

import com.tobeng.redis.manager.service.RedisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * redis 基本信息接口
 *
 * @author yr
 * @date 2019/09/13
 * @since 1.0.0
 */
@RestController
@RequestMapping("redis")
public class RedisController {

    @Resource
    private RedisService redisService;

    @GetMapping("tree")
    public Object getKeyTree(){
        return redisService.getKeyTree();
    }

    @GetMapping("/data/{key}")
    public Object getKey(@PathVariable String key){
        return redisService.getDataByKey(key);
    }

    @GetMapping("/data/match/{keyPrefix}")
    public List<Object> getMatchKey(@PathVariable String keyPrefix){
        return redisService.getMatchKey(keyPrefix);
    }

    @DeleteMapping("/data/{key}")
    public void deleteDataByKey(@PathVariable String key){
        redisService.deleteDataByKey(key);
    }

}
