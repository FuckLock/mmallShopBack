**mmall**

电商 项目后台接口

**技术**

**此项目是电商平台的接口项目，分别有用户模块开发,分类管理模块开发,商品管理模块开发,购物车模块开发,收货地址管理模块开发,支付模块开发,订单管理模块开发**

**Java框架采用如下**

Spring ``
 
SpringMvc

Mybatis




**涉及的核心技术有：**

tomcat集群

nginx负载均衡

redis分布式

redis分布式锁

Redisson 分布式锁

单点登录

Spring Schedule定时关单

google protostuff(用于序列化和反序列化，比java 内置的性能要好很多，可以考虑)




**目前演进过程**

_版本一：（完成）_

1.完成所有接口开发

2.进行线上部署tomcat(1台)


_版本二：（完成）_

1.登录和管理员权限使用三种方式实现:

第一： filter

第二： spring aop 

第三： springmvc 拦截器

2.session登录方式切换为 cookie + redis 方式(后面会进一步优化使用spring security)

3.添加了springmvc 全局异常处理, 使用两种方式实现

第一：springmvc自带的modelview方式

第二：自定义异常使用 @ControllerAdvice实现

4.添加redis分布式，使用两种方式实现

第一： 采用jedisSharedPool方式实现分布式（redis版本小于3.0）

第二： 采用redis-cluster集群方式实现分布式(redis 3.0后对这个支持了，这种方式在大型项目还需要考验)

5.Spring Schedule + redis 实现定时关单

6.redis分布式锁,两种方式实现

第一： redis原生的方式

第二：使用redission分布式锁（推荐）

7.tomcat集群方式搭建这个项目

_版本三：（更近进一步）（有时间的情况下正在做的）_

1.添加mockmvc测试对controller进行测试，进行测试开发

2.在测试的基础上对其他类进行进一步改造

3.对ServiceResponse使用重载方式，废弃以前不友好的方法

4.自己定义一个注解接口（可能对一些StringUtil判断的做些改造吧）

5.添加 swagger(既然是接口那就必须有这个了)

6.添加Spring Security进行改造登录系统

7.Spring Social 开发第三方登录  微信或者qq

8.Spring Security OAuth 进行认证

9.考虑高并发的情况下，使用多线程对项目中的一些逻辑进行改造。（比如订单定时取消这部分）

10. 自然是使用spring 4.0的，那就应该废弃配置方式，这个也需要改造

11.想不到了，先这样


 



