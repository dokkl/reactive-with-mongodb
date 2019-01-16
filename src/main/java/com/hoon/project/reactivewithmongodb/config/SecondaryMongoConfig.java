package com.hoon.project.reactivewithmongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Created by babybong on 09/01/2019.
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.hoon.project.reactivewithmongodb.repository.secondary",
        reactiveMongoTemplateRef = "secondaryMongoTemplate")
public class SecondaryMongoConfig {
}
