package com.lbj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author libaojie
 */
@SpringBootApplication
@MapperScan("com.lbj.mapper") //import tk.mybatis.spring.annotation.MapperScan;
public class Redis7StudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(Redis7StudyApplication.class, args);
    }

}
