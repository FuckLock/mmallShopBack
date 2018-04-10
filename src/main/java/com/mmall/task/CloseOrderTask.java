package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CloseOrderTask {

    private  static Logger logger = LoggerFactory.getLogger(CloseOrderTask.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

//    @Scheduled(cron="0 */1 * * * ?")//每1分钟(每个1分钟的整数倍)
//    public void closeOrderTaskV1(){
//        logger.info("关闭订单定时任务启动");
//        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//        iOrderService.closeOrder(hour);
//        logger.info("关闭订单定时任务结束");
//    }

//    @Scheduled(cron="0 */1 * * * ?")
//    public void closeOrderTaskV2(){
//        logger.info("关闭订单定时任务启动");
//        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
//
//        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
//        if(setnxResult != null && setnxResult.intValue() == 1){
//            //如果返回值是1，代表设置成功，获取锁
//            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        }else{
//            logger.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        }
//        logger.info("关闭订单定时任务结束");
//    }

//    @Scheduled(cron="0 */1 * * * ?")
//    public void closeOrderTaskV3(){
//        logger.info("关闭订单定时任务启动");
//        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
//        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
//        if(setnxResult != null && setnxResult.intValue() == 1){
//            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        }else{
//            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
//            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
//                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
//                //再次用当前时间戳getset。
//                //返回给定的key的旧值，->旧值判断，是否可以获取锁
//                //当key没有旧值时，即key不存在时，返回nil ->获取锁
//                //这里我们set了一个新的value值，获取旧的值。
//                if(getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr,getSetResult))){
//                    //真正获取到锁
//                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//                }else{
//                    logger.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//                }
//            }else{
//                logger.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//            }
//        }
//        logger.info("关闭订单定时任务结束");
//    }

    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTaskV4(){

        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            if(getLock = lock.tryLock(0,50, TimeUnit.SECONDS)){
                logger.info("Redisson获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//                iOrderService.closeOrder(hour);
            }else{
                logger.info("Redisson没有获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            logger.error("Redisson分布式锁获取异常",e);
        } finally {
            if(!getLock){
                return;
            }
            lock.unlock();
            logger.info("Redisson分布式锁释放锁");
        }
    }


    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,50);//有效期50秒，防止死锁
        logger.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        logger.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        logger.info("===============================");
    }
}
