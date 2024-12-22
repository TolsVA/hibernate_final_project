package com.javaRush.service;

import com.javaRush.domain.City;
import com.javaRush.redis.CityCountry;

import java.util.List;

public interface MysqlService {
    List<City> fetchData();
    void testMysqlData(List<Integer> ids);
    List<CityCountry> transformData(List<City> cities);
}
