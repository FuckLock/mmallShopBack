package com.mmall.util;


import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }


    //把对象序列化为json
    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.warn("Parse Object to String error",e);
            return null;
        }
    }

    //把对象序列化为漂亮的json
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            logger.warn("Parse Object to String error",e);
            return null;
        }
    }

    //把json在序列化成对象
    public static <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }

        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            logger.warn("Parse String to Object error",e);
            return null;
        }
    }



    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            logger.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?> elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            logger.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
//        User user1 = new User();
//        user1.setId(1);
//        user1.setUsername("erewrwer");
//        User user2 = new User();
//        user2.setId(2);
//        user2.setUsername("erewrwer2222");
////        String userJson = JsonUtil.obj2String(user);
////        String userJsonPretty = JsonUtil.obj2StringPretty(user);
////        logger.info("obj2String: {}", user.getClass());
////        logger.info("obj2String: {}", userJson);
////        logger.info("obj2StringPretty: {}", userJsonPretty);
//
////        JsonUtil.string2Obj(userJson, User.class);
////        logger.info("user: {}");
//        List<User> userList = Lists.newArrayList();
//        userList.add(user1);
//        userList.add(user2);
//        String userListStr = JsonUtil.obj2StringPretty(userList);
//        logger.info("userliststr: {}", userListStr);
//        List<User> userListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {});
//        logger.info("userListObj1: {}", userListObj1);
////        List<User> userListObj2 = JsonUtil.string2Obj(userListStr, List.class, User.class);
////        User userListObj3 = JsonUtil.string2Obj(userListStr, Set.class, User.class);
////
////        logger.info("userListObj2: {}", userListObj2);
////        logger.info("userListObj2: {}", userListObj3);

    }
}
