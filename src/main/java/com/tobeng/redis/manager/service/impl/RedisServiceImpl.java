package com.tobeng.redis.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tobeng.redis.manager.enums.DataTypeEnum;
import com.tobeng.redis.manager.enums.KeyTypeEnum;
import com.tobeng.redis.manager.service.RedisService;
import com.tobeng.redis.manager.vo.DataCacheVO;
import com.tobeng.redis.manager.vo.KeyCacheVO;
import com.tobeng.redis.manager.vo.KeyTreeVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis service 实现类
 *
 * @author yr
 * @date 2019/09/13
 * @since 1.0.0
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final String JOIN_SYMBOL = ":";

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Object getKeyTree() {
        Set<String> keys = stringRedisTemplate.keys("*");
        KeyTreeVO keyTreeVO = new KeyTreeVO(host + JOIN_SYMBOL + port, KeyTypeEnum.REDIS_DB.getCode());
        if (keys == null || keys.isEmpty()) {
            return Arrays.asList(keyTreeVO);
        }
        for (String key : keys) {
            if (!key.contains(JOIN_SYMBOL)) {
                KeyTreeVO children = new KeyTreeVO(key, KeyTypeEnum.REDIS_KEY.getCode());
                keyTreeVO.getChildren().add(children);
                continue;
            }
            dispose(keyTreeVO.getChildren(), key.split(JOIN_SYMBOL), 0, key);
        }
        return Arrays.asList(keyTreeVO);
    }

    @Override
    public Object getDataByKey(@NotNull String key) {
        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey == null || !hasKey) {
            return null;
        }
        DataType dataType = stringRedisTemplate.type(key);
        if (dataType == null) {
            dataType = DataType.fromCode("none");
        }
        DataCacheVO dataCacheVO = new DataCacheVO();
        dataCacheVO.setDateType(dataType.code());
        switch (dataType.code()) {
            case "string":
                Object objectString = stringRedisTemplate.opsForValue().get(key);
                if(objectString != null){
                    dataCacheVO.setCacheString(objectString.toString());
                }
                break;
            case "list":
                Long listSize = stringRedisTemplate.opsForList().size(key);
                if(listSize == null || listSize <= 0){
                    break;
                }
                List<String> range = stringRedisTemplate.opsForList().range(key, 0, listSize);
                if(range != null){
                    dataCacheVO.setCacheList(range);
                }
                break;
            case "set":
                Set<String> set = stringRedisTemplate.opsForSet().members(key);
                if(set != null){
                    dataCacheVO.setCacheSet(set);
                }
                break;
            case "zset":
                Long zsetSize = stringRedisTemplate.opsForZSet().size(key);
                if(zsetSize == null || zsetSize <= 0){
                    break;
                }
                Set<String> zset= stringRedisTemplate.opsForZSet().range(key, 0, zsetSize);
                if(zset != null){
                    dataCacheVO.setCacheZSet(zset);
                }
                break;
            case "hash":
                Map<Object, Object> objectMap = stringRedisTemplate.opsForHash().entries(key);
                if(objectMap != null){
                    dataCacheVO.setCacheMap(objectMap);
                }
                break;
            default:
        }
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        dataCacheVO.setExpire(expire);
        return dataCacheVO;
    }

    @Override
    public List<Object> getMatchKey(String keyPrefix) {
        Set<String> keys = stringRedisTemplate.keys(keyPrefix + "*");
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        List<Object> result = new ArrayList<>(keys.size());
        for (String key : keys) {
            KeyCacheVO keyCacheVO = new KeyCacheVO();
            keyCacheVO.setKey(key);
            DataType dataType = stringRedisTemplate.type(key);
            if (dataType == null) {
                dataType = DataType.fromCode("none");
            }
            if (!DataTypeEnum.STRING.getCode().equals(dataType.code()) &&
                    !DataTypeEnum.NONE.getCode().equals(dataType.code())) {
                Long size = null;
                switch (dataType.code()){
                    case "list":
                        size = stringRedisTemplate.opsForList().size(key);
                        break;
                    case "set":
                        size = stringRedisTemplate.opsForSet().size(key);
                        break;
                    case "zset":
                        size = stringRedisTemplate.opsForZSet().size(key);
                        break;
                    case "hash":
                        size = stringRedisTemplate.opsForHash().size(key);
                        break;
                    default:
                }
                if(size != null){
                    keyCacheVO.setTotal(size.intValue());
                }
            }
            keyCacheVO.setDateType(dataType.code());
            keyCacheVO.setExpire(stringRedisTemplate.getExpire(key, TimeUnit.SECONDS));
            result.add(keyCacheVO);
        }
        return result;
    }

    @Override
    public void deleteDataByKey(String key) {
        if (stringRedisTemplate.hasKey(key)) {
            stringRedisTemplate.delete(key);
        }
    }

    private void dispose(List<KeyTreeVO> list, String[] files, Integer index, String key) {
        if (index.equals(files.length - 1)) {
            KeyTreeVO keyTreeVO = new KeyTreeVO(key, KeyTypeEnum.REDIS_KEY.getCode());
            list.add(keyTreeVO);
            return;
        }
        String file = files[index];
        KeyTreeVO keyTreeVO = new KeyTreeVO(file, KeyTypeEnum.REDIS_FILE.getCode());
        if (list.isEmpty()) {
            list.add(keyTreeVO);
        } else {
            boolean b = true;
            for (KeyTreeVO treeVO : list) {
                if (treeVO.getName().equals(file) && treeVO.getType().equals(KeyTypeEnum.REDIS_FILE.getCode())) {
                    treeVO.totalAutoIncrement();
                    keyTreeVO = treeVO;
                    b = false;
                }
            }
            if (b) {
                list.add(keyTreeVO);
            }
        }
        dispose(keyTreeVO.getChildren(), files, index + 1, key);
    }
}
