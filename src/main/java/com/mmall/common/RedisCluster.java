package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.HashSet;
import java.util.Set;

// redis-3.0 采用如下方式写集群,使用redis cluster 在redis服务器端要建立集群
public class RedisCluster {

    private static JedisCluster jedisCluster;
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","20"));//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","20"));//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);
        Set<HostAndPort> clusterNodes = new HashSet<HostAndPort>();
        clusterNodes.add(new HostAndPort(redis1Ip, redis1Port));
        clusterNodes.add(new HostAndPort(redis2Ip, redis2Port));
        jedisCluster = new JedisCluster(clusterNodes, 5000, config);
    }

    public static JedisCluster getJedisCluster(){
        return jedisCluster;
    }

    static{
        initPool();
    }

    public static void returnResource(JedisCluster jedis){
        jedisCluster.close();
    }

//    public  static void  main(String[] args){
//        JedisCluster jedis = RedisCluster.getJedisCluster();
//
//        for(int i =0;i<10;i++){
//            jedis.set("key"+i,"value"+i);
//        }
//
//        System.out.println("program is end");
//    }
}
