package com.lbj.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Circle;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author libaojie
 */
@Service
@Slf4j
public class GeoService {
    public static final String CITY = "city";
    @Autowired
    private RedisTemplate redisTemplate;

    public String geoAdd() {
        HashMap<String, Point> map = new HashMap<>();
        map.put("天安门", new Point(116.403963, 39.915119));
        map.put("故宫", new Point(116.403414, 39.924091));
        map.put("长城", new Point(116.024067, 40.362639));
        redisTemplate.opsForGeo().add(CITY, map);
        return map.toString();
    }

    public Point position(String member) {
        List<Point> position = redisTemplate.opsForGeo().position(CITY, member);
        return position.get(0);
    }

    public String hash(String member) {
        List<String> hash = redisTemplate.opsForGeo().hash(CITY, member);
        return hash.get(0);
    }

    public Distance distance(String member1, String member2) {
        Distance distance = redisTemplate.opsForGeo().distance(CITY, member1, member2, Metrics.KILOMETERS);
        return distance;
    }

    public GeoResults radiusByxy() {
        // 人在三里屯 116.458347,39.940602
        Circle circle = new Circle(116.458347, 39.940602, Metrics.KILOMETERS.getMultiplier());
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance().includeCoordinates().sortDescending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo()
                .radius(CITY, circle, args);
        return radius;
    }

    public GeoResults radiusByMember() {
        //通过地方查找附近
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance().includeCoordinates().sortDescending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo()
                .radius(CITY,"天安门",new Distance(100,Metrics.KILOMETERS),args);

//        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo()
//                .radius(CITY, "天安门", 100000);
        return radius;
    }
}
