package com.m7.abs.support.common.util;


import com.m7.abs.support.common.constant.AbsSupportProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LockUtils {
    @Autowired
    private AbsSupportProperties absSupportProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁
     *
     * @param key     key
     * @param expired 过期时间  单位毫秒
     * @param batchId 执行批次号
     * @return true/false
     */
    @SuppressWarnings("all")
    private boolean tryLock(String key, long expired, String batchId) {
        try {
            return (Boolean) stringRedisTemplate.execute((RedisCallback) connection -> {
                long expireAt = System.currentTimeMillis() + expired + 1;
                Boolean acquire = connection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());
                if (acquire.booleanValue()) {
                    return Boolean.TRUE;
                } else {
                    byte[] value = connection.get(key.getBytes());
                    if (Objects.nonNull(value) && value.length > 0) {
                        long expireTime = Long.parseLong(new String(value));
                        if (expireTime < System.currentTimeMillis()) {
                            // 如果已经过期
                            long newExpireAt = System.currentTimeMillis() + expired + 1;
                            byte[] oldValue = connection.getSet(key.getBytes(), String.valueOf(newExpireAt).getBytes());
                            // 防止死锁
                            return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                        }
                    }
                }
                return Boolean.FALSE;
            });
        } catch (Exception e) {
            log.error("[" + batchId + "] lock key:[" + key + "] exception:" + e.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 上锁
     *
     * @param key     key
     * @param batchId 执行批次号
     * @return true/false
     */
    public boolean lock(String key, String batchId) {
        boolean flag = false;
        try {
            // 没有获取到锁时 重复获取4次  每次间隔100ms
            for (int index = 1; index < 6; index++) {
                flag = tryLock(key, absSupportProperties.getMidNumTask().getLockExpiration(), batchId);
                if (flag) {
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.error("[" + batchId + "] lock key:[" + key + "] exception:" + e.getMessage());
            flag = false;
        }
        //log.info("[" + batchId + "] lock key:[" + key + "] result:[" + flag + "]");
        return flag;
    }

    /**
     * 释放锁
     *
     * @param key     key
     * @param batchId 操作id
     */
    public void unLock(String key, String batchId) {
        try {
            stringRedisTemplate.delete(key);
            //log.info("[" + batchId + "] unlock key:[" + key + "] result:[success]");
        } catch (Exception e) {
            log.error("[" + batchId + "] unlock key:[" + key + "] exception:" + e.getMessage());
        }
    }

    /**
     * 上锁
     *
     * @param key     key
     * @param expire  过期时间
     * @param batchId 执行批次号
     * @return true/false
     */
    public boolean lockJob(String key, long expire, String batchId) {
        boolean flag;
        try {
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, batchId, expire, TimeUnit.MILLISECONDS);
            flag = result != null && result;
        } catch (Exception e) {
            log.error("[" + batchId + "] lockJob key:[" + key + "] exception:" + e.getMessage());
            flag = false;
        }
        log.info("[" + batchId + "] lockJob key:[" + key + "] result:[" + flag + "]");
        return flag;
    }

    /**
     * 释放锁 存入的value是一致的才释放，避免释放别人加的锁
     *
     * @param key     key
     * @param batchId 执行批次号
     * @return true/false
     */
    public boolean unLockJob(String key, String batchId) {
        boolean flag;
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return false end";
            Boolean result = stringRedisTemplate.execute(
                    connection -> connection.eval(
                            script.getBytes(),
                            ReturnType.BOOLEAN,
                            1,
                            key.getBytes(),
                            batchId.getBytes())
                    , true);
            flag = result != null && result;
        } catch (Exception e) {
            log.error("[" + batchId + "] unlockJob key:[" + key + "] exception:" + e.getMessage());
            flag = false;
        }
        log.info("[" + batchId + "] unlockJob key:[" + key + "] result:[" + flag + "]");
        return flag;
    }
}
