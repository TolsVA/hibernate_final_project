package com.javaRush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaRush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RedisServiceImpl implements RedisService {
    private final RedisClient redisClient;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    public RedisServiceImpl() {
        redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        mapper = new ObjectMapper();
    }

    @Override
    public void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    String key = String.valueOf(cityCountry.getId());
                    String value = mapper.writeValueAsString(cityCountry);
                    sync.set(key, value);  // Запись данных в Redis
                } catch (JsonProcessingException e) {
                    logger.error("Error", e);
                }
            }
        }
    }

    @Override
    public void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    logger.error("Error", e);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        redisClient.shutdown();
    }
}
