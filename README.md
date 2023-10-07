# 分布式锁starter
  通过Redis实现分布式锁，封装成starter开箱即用，支持通过注解实现分布式锁，key支持通过SPEL表达式自定义。

## 使用
1. 引入依赖

   ```xml
           <dependency>
               <groupId>io.github.xw-zzz</groupId>
               <artifactId>xw-distributed-lock-starter</artifactId>
               <version>1.0.0-RELEASE</version>
           </dependency>
   ```

2. 在Springboot主类`@EnableAspectJAutoProxy`注解

   ```java
   @SpringBootApplication()
   @EnableAspectJAutoProxy
   public class DistridubutedLockTestApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(DistridubutedLockTestApplication.class, args);
       }
   
   }
   
   ```

3. 配置redis配置

   ```properties
   spring.redis.host=xxxx
   spring.redis.port=6379
   spring.redis.password=xxx
   ```

4. 使用案例

   * 支持SPEL表达式设定key

   ```java
       @GetMapping("/test/lock")
       @DistributedLock(lockKey = "111",value = "2222",throwExceptionWhenLockFailed = true)
       public void lock() throws InterruptedException {
           System.out.println("加锁成功！！！！！！！！");
           TimeUnit.SECONDS.sleep(111); 
       }
   
       @GetMapping("/test/lock1/{name}")
       @DistributedLock(lockKey = "#name",value = "2222",throwExceptionWhenLockFailed = true)
       public void lock1(@PathVariable String name) throws InterruptedException {
           System.out.println("加锁成功！！！！！！！！");
           TimeUnit.SECONDS.sleep(111); 
       }
   ```

   

