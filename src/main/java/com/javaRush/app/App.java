package com.javaRush.app;

import com.javaRush.config.AppConfig;
import com.javaRush.service.MysqlService;
import com.javaRush.service.RedisService;
import com.javaRush.view.View;
import com.javaRush.view.Viewable;

public class App {
    private final Viewable view;

    private static class SingletonHelper {
        private static final App INSTANCE = new App();
    }

    private App() {
        MysqlService mysqlService = AppConfig.createMysqlService();
        RedisService redisService = AppConfig.createRedisService();
        view = new View(mysqlService, redisService);
    }

    public static void startProject() {
        SingletonHelper.INSTANCE.view.run();
    }
}
