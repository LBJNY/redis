/**
 * JedisDemo.class
 * Created By LiBaoJie
 * Created At 2023年05月04日
 *
 * @Description:
 */
package com.lbj.demo;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * JedisDemo
 *
 * @author LiBaoJie
 */
public class JedisDemo {
    public static void main(String[] args) {
        //1。获取连接
        Jedis jedis = new Jedis("124.222.229.179", 6379);

        //2.设置密码
        jedis.auth("");

        //3.操作
        System.out.println(jedis.ping());


        jedis.set("key1", "value1");
        String key1 = jedis.get("key1");
        System.out.println(key1);

        jedis.lpush("list", "l1", "l2");
        List<String> list = jedis.lrange("list", 0, -1);
        for (String item : list) {
            System.out.println(item);
        }

        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
    }
}
