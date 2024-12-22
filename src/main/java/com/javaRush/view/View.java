package com.javaRush.view;

import com.javaRush.domain.City;
import com.javaRush.redis.CityCountry;
import com.javaRush.service.MysqlService;
import com.javaRush.service.RedisService;

import java.util.List;

public class View implements Viewable{
    private final MysqlService mysqlService;
    private final RedisService redisService;

    public View(MysqlService mysqlService, RedisService redisService) {
        this.mysqlService = mysqlService;
        this.redisService = redisService;
    }

    @Override
    public void run() {
        // Получаем все города из базы данных
        List<City> allCities = mysqlService.fetchData();
        // Преобразуем данные в формат для Redis
        List<CityCountry> preparedData = mysqlService.transformData(allCities);
        // Записываем данные в Redis
        redisService.pushToRedis(preparedData);

        //выбираем случайных 10 id городов
        //так как мы не делали обработку невалидных ситуаций, используй существующие в БД id
        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long stopRedis = measureExecutionTime(() -> redisService.testRedisData(ids));

        // Замеряем время для MySQL
        long stopMysql = measureExecutionTime(() -> mysqlService.testMysqlData(ids));

        // Выводим результаты
        System.out.printf("Redis:\t%d ms\n", stopRedis);
        System.out.printf("MySQL:\t%d ms\n", stopMysql);
        shutdown();
    }

    // Метод для замера времени выполнения
    private long measureExecutionTime(Runnable action) {
        long start = System.currentTimeMillis();
        action.run();
        return System.currentTimeMillis() - start;
    }

    private void shutdown() {
        redisService.shutdown();
    }
}
