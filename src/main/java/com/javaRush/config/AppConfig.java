package com.javaRush.config;

import com.javaRush.service.MysqlServiceImpl;
import com.javaRush.service.RedisServiceImpl;

public class AppConfig {
    public static MysqlServiceImpl createMysqlService() {
        return new MysqlServiceImpl();
    }

    public static RedisServiceImpl createRedisService() {
        return new RedisServiceImpl();
    }
}
