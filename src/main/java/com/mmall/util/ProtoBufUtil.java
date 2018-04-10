package com.mmall.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.mmall.common.RedisShardedPool;
import com.mmall.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;


// 扩展类，用于序列化对象，性能上比java内置的序列化要好很多, 可以用这中方式+ redis 实现用户登录方式更佳
public class ProtoBufUtil {

    private static Logger logger = LoggerFactory.getLogger(ProtoBufUtil.class);

    // 序列化
    public static <T> byte[] serializer(T o) {
        Schema schema = RuntimeSchema.getSchema(o.getClass());
        return ProtobufIOUtil.toByteArray(o, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    // 反序列化
    public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        T obj = null;
        try {
            Schema schema = RuntimeSchema.getSchema(clazz);
            obj = (T) schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        } catch (Exception e) {
            logger.error("protobut 反序列化异常: {}", e);
        }

        return obj;
    }

    public  static void main(String[] args) {
//        User user = new User();
//        user.setId(1);
//        user.setUsername("baodongfsdfasdfsadfsdfsdfdsafdsafsadfsadfasdfasdfsadfs");
//        String key = "baosdsd";
//        byte[] bytes = ProtoBufUtil.serializer(user);
//
//        ShardedJedis jedis = RedisShardedPool.getJedis();
//
//        jedis.set(key.getBytes(), bytes);
//
//        byte[] bytes1 = jedis.get(key.getBytes());
//
//        User user1 = ProtoBufUtil.deserializer(bytes1, User.class);
//
//
//        System.out.println("用户名id: " + user1.getId() + "用户名：" + user1.getUsername());

    }

}
