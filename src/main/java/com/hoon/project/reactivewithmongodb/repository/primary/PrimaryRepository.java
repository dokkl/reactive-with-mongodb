package com.hoon.project.reactivewithmongodb.repository.primary;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by babybong on 09/01/2019.
 */
public interface PrimaryRepository extends ReactiveMongoRepository<PrimaryModel, String> {
}
