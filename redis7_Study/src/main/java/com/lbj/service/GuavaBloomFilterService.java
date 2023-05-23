package com.lbj.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @auther lbj
 */
@Service
@Slf4j
public class GuavaBloomFilterService {

    /**
     * 测试hash冲突
     */
    @Test
    public void testHash() {
        //非hash冲突
        System.out.println("非Hash冲突：");
        System.out.println("AA".hashCode());
        System.out.println("BB".hashCode());

        //hash冲突
        System.out.println("Hash冲突：");
        System.out.println("Aa".hashCode());
        System.out.println("BB".hashCode());

        System.out.println("柳柴".hashCode());
        System.out.println("柴柕".hashCode());

        System.out.println();

        Set<Integer> sets = new HashSet<>();
        int hashCode;
        for (int i = 0; i < 200000; i++)
        {
            hashCode = new Object().hashCode();
            if(sets.contains(hashCode))
            {
                System.out.println("运行到第: "+i+" 次出现hash冲突,hashcode: "+hashCode);
                continue;
            }
            sets.add(hashCode);
        }
    }

    public void guavaBloomFilter() {
    }
}
