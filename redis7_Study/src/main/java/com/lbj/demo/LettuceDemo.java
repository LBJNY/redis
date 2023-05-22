/**
 * LettuceDemo.class
 * Created By LiBaoJie
 * Created At 2023年05月05日
 *
 * @Description:
 */
package com.lbj.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;

/**
 * LettuceDemo
 *
 * @author LiBaoJie
 */
public class LettuceDemo {
    public static void main(String[] args) {
        //1.使用链式编程builder  RedisRRI
        RedisURI uri = RedisURI.Builder
                .redis("124.222.229.179")
                .withPort(6379)
                .withAuthentication("default", "lbj0307")
                .build();
        //2.创建链接客户端
        RedisClient redisClient = RedisClient.create(uri);
        StatefulRedisConnection connect = redisClient.connect();
        //3.创建操作的command
        RedisCommands sync = connect.sync();

        //操作

        sync.set("key1", "value1");
        String key1 = (String) sync.get("key1");
        System.out.println(key1);

        sync.lpush("list", "l1", "l2");
        List list = sync.lrange("list", 0, -1);
        for (Object item : list) {
            System.out.println(item);
        }

        List keys = sync.keys("*");
        System.out.println(keys);
        //4.关闭释放资源
        connect.close();
        redisClient.close();
    }
}
