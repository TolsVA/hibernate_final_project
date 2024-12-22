package com.javaRush.transformers;

import com.javaRush.domain.City;
import com.javaRush.domain.Country;
import com.javaRush.domain.CountryLanguage;
import com.javaRush.redis.CityCountry;
import com.javaRush.redis.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class CityToCityCountryTransformer {
    private static final Logger logger = LoggerFactory.getLogger(CityToCityCountryTransformer.class);

    public CityCountry transform(City city) {
        CityCountry res = new CityCountry();

        // Преобразуем информацию из объекта City в объект CityCountry
        res.setId(city.getId());
        res.setName(city.getName());
        res.setPopulation(city.getPopulation());
        res.setDistrict(city.getDistrict());

        // Преобразуем данные о стране (Country), связанной с городом
        Country country = city.getCountry();
        res.setAlternativeCountryCode(country.getAlternativeCode());
        res.setContinent(country.getContinent());
        res.setCountryCode(country.getCode());
        res.setCountryName(country.getName());
        res.setCountryPopulation(country.getPopulation());
        res.setCountryRegion(country.getRegion());
        res.setCountrySurfaceArea(country.getSurfaceArea());

        // Преобразуем языки страны
        Set<CountryLanguage> countryLanguages = country.getLanguages();
        Set<Language> languages = countryLanguages.stream()
                .map(cl -> new Language(cl.getLanguage(), cl.getIsOfficial(), cl.getPercentage()))
                .collect(Collectors.toSet());
        res.setLanguages(languages);

        logger.debug("Transformed city {} to CityCountry.", city.getName());
        return res;
    }
}

