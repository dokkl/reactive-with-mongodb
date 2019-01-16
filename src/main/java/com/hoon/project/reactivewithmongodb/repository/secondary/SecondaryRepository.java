package com.hoon.project.reactivewithmongodb.repository.secondary;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by babybong on 09/01/2019.
 */
public interface SecondaryRepository extends ReactiveMongoRepository<SecondaryModel, String> {
}
