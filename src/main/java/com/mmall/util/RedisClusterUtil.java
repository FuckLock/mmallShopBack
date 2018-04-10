package com.mmall.util;

import com.mmall.common.RedisCluster;
import com.mmall.common.RedisShardedPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

public class RedisClusterUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisClusterUtil.class);

    public static Long expire(String key,int exTime){
        JedisCluster jedis = null;
        Long result = null;
        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            logger.error("expire key:{} error",key,e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒
    public static String setEx(String key,String value,int exTime){
        JedisCluster jedis = null;
        String result = null;
        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            logger.error("setex key:{} value:{} error", key, value, e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }

    public static String set(String key,String value){
        JedisCluster jedis = null;
        String result = null;

        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.set(key, value);
        } catch (Exception e) {
            logger.error("set key:{} value:{} error",key,value,e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        JedisCluster jedis = null;
        String result = null;
        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.get(key);
        } catch (Exception e) {
            logger.error("get key:{} error",key,e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        JedisCluster jedis = null;
        Long result = null;
        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.del(key);
        } catch (Exception e) {
            logger.error("del key:{} error",key,e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }

    public static Long setnx(String key,String value){
        JedisCluster jedis = null;
        Long result = null;

        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.setnx(key,value);
        } catch (Exception e) {
            logger.error("setnx key:{} value:{} error",key,value,e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }

    public static String getSet(String key, String value) {
        JedisCluster jedis = null;
        String result = null;
        try {
            jedis = RedisCluster.getJedisCluster();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            logger.error("getSet key:{} error",key,e);
            RedisCluster.returnResource(jedis);
            return result;
        }
        RedisCluster.returnResource(jedis);
        return result;
    }
}
