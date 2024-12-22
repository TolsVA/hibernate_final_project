package com.javaRush.service;

import com.javaRush.dao.CityDAO;
import com.javaRush.dao.CountryDAO;
import com.javaRush.domain.City;
import com.javaRush.domain.Country;
import com.javaRush.domain.CountryLanguage;
import com.javaRush.redis.CityCountry;
import com.javaRush.transformers.CityToCityCountryTransformer;
import com.javaRush.util.HibernateUtil;
import com.javaRush.util.SessionUtil;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MysqlServiceImpl implements MysqlService {
    private static final Logger logger = LoggerFactory.getLogger(MysqlServiceImpl.class);
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final SessionUtil sessionUtil;
    private final CityToCityCountryTransformer transformer;

    public MysqlServiceImpl() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        this.cityDAO = new CityDAO(sessionFactory);
        this.countryDAO = new CountryDAO(sessionFactory);
        this.sessionUtil = new SessionUtil(sessionFactory);
        this.transformer = new CityToCityCountryTransformer();
    }

    @Override
    public List<City> fetchData() {
        logger.info("Fetching cities from database...");
        return sessionUtil.executeInTransaction(session -> {
            List<City> allCities = new ArrayList<>();
            try {
                List<Country> countries = countryDAO.getAll();
                int totalCount = cityDAO.getTotalCount();
                int step = 500;
                for (int i = 0; i < totalCount; i += step) {
                    logger.debug("Fetching cities, step: {}, total: {}", i, totalCount);
                    allCities.addAll(cityDAO.getItems(i, step));
                }
            } catch (Exception e) {
                logger.error("Error occurred while fetching cities", e);
            }
            return allCities;
        });
    }

    @Override
    public void testMysqlData(List<Integer> ids) {
        logger.info("Testing MySQL data for {} city ids", ids.size());
        sessionUtil.executeInTransaction(session -> {
            for (Integer id : ids) {
                try {
                    City city = cityDAO.getById(id);
                    if (city != null) {
                        Set<CountryLanguage> languages = city.getCountry().getLanguages();
                        logger.debug("City with id {} has {} languages.", id, languages.size());
                    } else {
                        logger.warn("City with id {} not found", id);
                    }
                } catch (Exception e) {
                    logger.error("Error occurred while processing city with id {}", id, e);
                }
            }
            return null;
        });
    }

    @Override
    public List<CityCountry> transformData(List<City> cities) {
        logger.info("Transforming {} cities to CityCountry objects", cities.size());
        return cities.stream().map(transformer::transform).collect(Collectors.toList());
    }
}