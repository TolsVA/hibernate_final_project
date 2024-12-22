package com.javaRush.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Language {
    private String language;
    private Boolean isOfficial;
    private BigDecimal percentage;
}
