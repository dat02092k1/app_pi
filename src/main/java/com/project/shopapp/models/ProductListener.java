package com.project.shopapp.models;

import com.project.shopapp.services.product.IProductRedisService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ProductListener implements ApplicationContextAware {
    private static IProductRedisService productRedisService;
    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);

    public ProductListener() {
    }

    @PrePersist
    public void prePersist(Product product) {
        logger.info("prePersist");
    }

    @PostPersist //save = persis
    public void postPersist(Product product) {
        // Update Redis cache
        logger.info("postPersist");
        productRedisService.clearCache();
    }

    @PreUpdate
    public void preUpdate(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Product product) {
        logger.info("postUpdate");
        productRedisService.clearCache();
    }

    @PreRemove
    public void preRemove(Product product) {
        logger.info("preRemove");
    }

    @PostRemove
    public void postRemove(Product product) {
        logger.info("postRemove");
        productRedisService.clearCache();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        productRedisService = applicationContext.getBean(IProductRedisService.class);
    }
}
