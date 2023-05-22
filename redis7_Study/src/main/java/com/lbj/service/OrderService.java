/**
 * OrderService.class
 * Created By LiBaoJie
 * Created At 2023年05月05日
 *
 * @Description:
 */
package com.lbj.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * OrderService
 *
 * @author LiBaoJie
 */
@Service
@Slf4j
public class OrderService {

    public final static String ORDER_KEY = "order:";
    @Autowired
    private RedisTemplate redisTemplate;

    public void addOrder() {
        int id = ThreadLocalRandom.current().nextInt(1000) + 1;
        String serialNo = UUID.randomUUID().toString();
        String key = ORDER_KEY + id;
        String value = "测试订单" + serialNo;
        redisTemplate.opsForValue().set(key, value);
        log.info("key:{}", key);
        log.info("value:{}", value);
    }

    public String getOrderById(Integer id) {
        return Objects.requireNonNull(redisTemplate.opsForValue().get(ORDER_KEY + id)).toString();
    }
}
