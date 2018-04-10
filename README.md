mmall

电商 项目后台接口

**技术**

**此项目是电商平台的接口项目，分别有用户模块开发,分类管理模块开发,商品管理模块开发,购物车模块开发,收货地址管理模块开发,支付模块开发,订单管理模块开发**

`Java框架采用如下`

Spring ``
 
SpringMvc

Mybatis




`涉及的核心技术有：`

tomcat集群

nginx负载均衡

redis分布式

redis分布式锁

Redisson 分布式锁

单点登录

Spring Schedule定时关单

google protostuff(用于序列化和反序列化，比java 内置的性能要好很多，可以考虑)

`目前演进过程`

1.初期只是完成一些接口开发

2.完成所有接口,进行线上部署tomcat(1台)

3.对代码进行重构，比如登录和管理员权限控制，添加了springmvc 拦截器来实现，前期使用的是spring aop实现的都可以

4.添加单点的登录, 使用cookie + redis 方式来实现登录

5.添加了springmvc 全局异常处理

6.springmvc restful方式改造（目前没做）

7.添加redis分布式,此时redis版本为2.8.0,不支持redis-cluster集群，采用 jedissharedpool方式实现分布式，redis服务器
不需要特殊的要求，开启几个端口就行

8.redis版本提升到3.7.0, 因为redis.3.0后支持redis-cluster，所以改用这种方式. Java实现使用jediscluster,这种
方式需要对redis服务器进行集群.

9.Spring Schedule + redis 实现定时关单

10.redisson分布式锁

11.tomcat集群方式搭建这个项目 



`不足点，还需要来点味道，接下来需要做的`

Spring Security 实现登录

Spring Security 控制授权

Spring Social 开发第三方登录  微信或者qq

Spring Security OAuth 进行认证

redis 主从复制

redis Sentinel

nginx 进行进一步调整

代码进行进一步优化


目前先这样吧。。。。。。。。。。

