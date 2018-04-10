package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedissonManager {

    public static Logger logger = LoggerFactory.getLogger(RedissonManager.class);
    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    @PostConstruct
    private void init(){
        try {
//            单台redis
//            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());

            config.useClusterServers().addNodeAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString(),
                                                        new StringBuilder().append(redis2Ip).append(":").append(redis2Port).toString());

            redisson = (Redisson) Redisson.create(config);

            logger.info("初始化Redisson结束");
        } catch (Exception e) {
            logger.error("redisson init error",e);
        }
    }
}

