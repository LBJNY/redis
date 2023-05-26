package com.lbj.service;


import com.lbj.entity.Customer;
import com.lbj.mapper.CustomerMapper;
import com.lbj.utils.CheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author libaojie
 */
@Service
@Slf4j
public class CustomerService {
    public static final String CACHA_KEY_CUSTOMER = "customer:";

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CheckUtils checkUtils;

    /**
     * 写操作
     *
     * @param customer
     */
    public void addCustomer(Customer customer) {
        int i = customerMapper.insertSelective(customer);

        if (i > 0) {
            //mysql插入成功，需要重新查询一次将数据捞出来，写进redis
            Customer result = customerMapper.selectByPrimaryKey(customer.getId());
            //redis缓存key
            String key = CACHA_KEY_CUSTOMER + customer.getId();
            //捞出来的数据写进redis
            redisTemplate.opsForValue().set(key, result);
        }
    }

    public Customer findCustomerById(Integer customreId) {
        Customer customer = null;
        //缓存redis的key名称
        String key = CACHA_KEY_CUSTOMER + customreId;

        //1 先去redis查询
        customer = (Customer) redisTemplate.opsForValue().get(key);

        //2 redis有直接返回，没有再进去查询mysql
        if (customer == null) {
            // 3 再去查询我们的mysql
            customer = customerMapper.selectByPrimaryKey(customreId);
            // 3.1 mysql有，redis无
            if (customer != null) {
                //3.2 把mysq查询出来的数据回写redis，保持一致性
                redisTemplate.opsForValue().set(key, customer);
            }
        }
        return customer;
    }

    /**
     * BloomFilter → redis → mysql
     * 白名单：whitelistCustomer
     *
     * @param customerId
     * @return
     */
    public Customer findCustomerByIdWithBloomFilter(Integer customerId) {
        Customer customer = null;
        //缓存redis的key名称
        String key = CACHA_KEY_CUSTOMER + customerId;
        if (checkUtils.checkWithBloomFilter("whitelistCustomer", key)) {
            System.out.println("白名单无此顾客：" + customerId);
        }

        //1 先去redis查询
        customer = (Customer) redisTemplate.opsForValue().get(key);

        //2 redis有直接返回，没有再进去查询mysql
        if (customer == null) {
            // 3 再去查询我们的mysql
            customer = customerMapper.selectByPrimaryKey(customerId);
            // 3.1 mysql有，redis无
            if (customer != null) {
                //3.2 把mysq查询出来的数据回写redis，保持一致性
                redisTemplate.opsForValue().set(key, customer);
            }
        }
        return customer;
    }

}