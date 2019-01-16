package com.hoon.project.reactivewithmongodb.config;

import com.mongodb.ConnectionString;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by babybong on 09/01/2019.
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({MultipleMongoProperties.class})
@Slf4j
public class MultipleMongoConfig {
    private final MultipleMongoProperties mongoProperties;

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public ReactiveMongoTemplate primaryMongoTemplate() throws Exception {
        ReactiveMongoTemplate rt = new ReactiveMongoTemplate(primaryFactory(this.mongoProperties.getPrimary()));
        //rt.setWriteConcernResolver(new MyAppWriteConcernResolver());
        rt.setWriteConcern(WriteConcern.W1.withWTimeout(200L, TimeUnit.MILLISECONDS).withJournal(true)); //쓰기를 승인하기전에 저널을 디스크에 강제로 동기화할수있다.
        rt.setReadPreference(ReadPreference.secondaryPreferred()); //세컨더리에서 읽기 수행, 만약 문제가 발생하면 읽기를 프라이머리에서 수행
        log.info("primaryMongoTemplate : {}", mongoProperties.getPrimary().getHost());
        return rt;
    }

    @Bean(name = "secondaryMongoTemplate")
    public ReactiveMongoTemplate secondaryMongoTemplate() throws Exception {
        ReactiveMongoTemplate rt = new ReactiveMongoTemplate(secondaryFactory(this.mongoProperties.getSecondary()));
        rt.setWriteConcern(WriteConcern.W1.withWTimeout(200L, TimeUnit.MILLISECONDS).withJournal(true)); //쓰기를 승인하기전에 저널을 디스크에 강제로 동기화할수있다.
        rt.setReadPreference(ReadPreference.primary()); //프라이머리에서 읽기 수행. 읽기 일관성 보장
        log.info("secondaryMongoTemplate : {}", mongoProperties.getSecondary().getHost());
        return rt;
    }

    /*@Primary
    @Bean
    public ReactiveMongoDatabaseFactory primaryFactory(final MongoProperties mongo) throws Exception {
        log.info("primaryFactory : {}", mongo.getHost());
        return new SimpleReactiveMongoDatabaseFactory(MongoClients.create("mongodb://" + mongo.getHost() + ":" + mongo.getPort()), mongo.getDatabase());
    }*/

    @Primary
    @Bean
    public ReactiveMongoDatabaseFactory primaryFactory(final MongoProperties mongo) throws Exception {
        log.info("primaryFactory : {}, {}, {}", mongo.getHost(), mongo.getUsername(), String.valueOf(mongo.getPassword()));
        return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(mongo.getUri()));
       /* StringBuilder sb = new StringBuilder("mongodb://");
        sb.append(mongo.getUsername()).append(":");
        sb.append(String.valueOf(mongo.getPassword())).append("@");
        sb.append(mongo.getHost()).append(":");
        sb.append(mongo.getPort()).append("/");
        sb.append(mongo.getDatabase());
        return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(sb.toString()));*/
    }

    @Bean
    public ReactiveMongoDatabaseFactory secondaryFactory(final MongoProperties mongo) throws Exception {
        log.info("secondaryFactory : {}, {}, {}", mongo.getHost(), mongo.getUsername(), String.valueOf(mongo.getPassword()));
        //return new SimpleReactiveMongoDatabaseFactory(MongoClients.create("mongodb://" + mongo.getHost() + ":" + mongo.getPort()), mongo.getDatabase());
        return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(mongo.getUri()));

        /*StringBuilder sb = new StringBuilder("mongodb://");
        sb.append(mongo.getUsername()).append(":");
        sb.append(String.valueOf(mongo.getPassword())).append("@");
        sb.append(mongo.getHost()).append(":");
        sb.append(mongo.getPort()).append("/");
        sb.append(mongo.getDatabase());
        return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(sb.toString()));*/
    }

    /*@Bean
    public ReactiveMongoDatabaseFactory secondaryFactory(final MongoProperties mongo) throws Exception {
        log.info("secondaryFactory : {}", mongo.getHost());
        return new SimpleReactiveMongoDatabaseFactory(MongoClients.create("mongodb://" + mongo.getHost() + ":" + mongo.getPort()), mongo.getDatabase());
    }*/

    /*private class MyAppWriteConcernResolver implements WriteConcernResolver {

        public WriteConcern resolve(MongoAction action) {
            if (action.getEntityType().getSimpleName().contains("Audit")) {
                return WriteConcern.ACKNOWLEDGED;
            } else if (action.getEntityType().getSimpleName().contains("Metadata")) {
                return WriteConcern.JOURNAL_SAFE;
            }
            return action.getDefaultWriteConcern();
        }
    }*/

}
