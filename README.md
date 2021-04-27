# seckill
一个商品秒杀系统
# 技术栈
* Springboot
* SpringMVC
* Redis
* RabbitMQ
# 可以优化的方向
1. 缓存
   1. 页面缓存
   2. 对象缓存
   3. 页面静态化
   4. CDN
2. 静态资源优化
   1. js/css压缩
   2. 多个js/css组合，减少连接数([tengine](https://github.com/alibaba/tengine)、[webpack](https://github.com/webpack/webpack)支持)
3. 接口优化(**核心思路：减少数据库访问**)
   1. Redis预减库存减少数据库访问
   2. 内存标记减少Redis访问
   3. 请求先入队缓冲([RabbitMQ](https://github.com/rabbitmq/rabbitmq-server))，异步下单，增强用户体验
   4. Nginx水平扩展
   5. mycat 分库分表中间件
# 超卖问题
解决方法
1. 数据库加唯一索引:防止统一用户重复购买
2. SQL添加库存数量判断:防止库存变成负数
# 接口优化的步骤
**server端**
1. 系统初始化，把商品库存数量加载到Redis
2. 收到请求，Redis预减库存，库存不足，直接返回，否则进入3
3. 请求入队，立即返回**排队中**
4. 请求出队，生成订单，减少库存

**client端**
5. 客户端轮询，是否秒杀成功


   
