package com.javaRush.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static volatile SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new Configuration()
                                .configure("hibernate.cfg.xml")
                                .buildSessionFactory();
                    } catch (Exception e) {
                        logger.error("Initial SessionFactory creation failed.", e);
                        throw new ExceptionInInitializerError("Initial SessionFactory creation failed." + e);
                    }
                }
            }
        }
        return sessionFactory;
    }
}