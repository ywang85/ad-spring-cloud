package com.imooc.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
// 想要在DataTable应用ApplicationContext
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext context;

    private static final Map<Class, Object> dataTableMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.context = applicationContext;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    public static <T> T of(Class<T> clazz) {
        T instance = (T) dataTableMap.get(clazz);
        if (instance != null) {
            return instance;
        }
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    private static <T> T bean(String beanName) {
        return (T) context.getBean(beanName);
    }

    private static <T> T bean(Class clazz) {
        return (T) context.getBean(clazz);
    }
}
