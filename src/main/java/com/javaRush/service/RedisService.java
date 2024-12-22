package com.javaRush.service;

import com.javaRush.redis.CityCountry;

import java.util.List;

public interface RedisService {
    void pushToRedis(List<CityCountry> data);
    void testRedisData(List<Integer> ids);
    void shutdown();
}
