package com.hoon.project.reactivewithmongodb;

import com.hoon.project.reactivewithmongodb.repository.BaseEntity;
import com.hoon.project.reactivewithmongodb.repository.Blog;
import com.hoon.project.reactivewithmongodb.repository.primary.BlogRepository;
import com.hoon.project.reactivewithmongodb.repository.primary.PrimaryModel;
import com.hoon.project.reactivewithmongodb.repository.primary.PrimaryRepository;
import com.hoon.project.reactivewithmongodb.repository.secondary.SecondaryModel;
import com.hoon.project.reactivewithmongodb.repository.secondary.SecondaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class
        //, MongoAutoConfiguration.class
        //, MongoDataAutoConfiguration.class
        //, MongoRepositoriesAutoConfiguration.class
})
public class ReactiveWithMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveWithMongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner run(PrimaryRepository primaryRepository
            , SecondaryRepository secondaryRepository
            , BlogRepository blogRepository) {
        return args -> {
            log.info("************************************************************");
            log.info("Start printing mongo objects");
            log.info("************************************************************");
            Mono<PrimaryModel> pp = primaryRepository.save(new PrimaryModel(null, "Primary 디비에 들어갑니다.3"));
            log.info("pp : {}", pp.block().toString());

            Mono<SecondaryModel> ss = secondaryRepository.save(new SecondaryModel(null, "Secondary 디비에 들어갑니다.3"));
            log.info("ss : {}", ss.block().toString());

            Flux<PrimaryModel> primaries = primaryRepository.findAll();
            for (PrimaryModel primary : primaries.collectList().block()) {
                log.info("primary >> {}", primary.toString());
            }
            Flux<SecondaryModel> secondaries = secondaryRepository.findAll();
            for (SecondaryModel secondary : secondaries.collectList().block()) {
                log.info("secondary >> {}", secondary.toString());
            }
            log.info("************************************************************");
            log.info("Ended printing mongo objects");
            log.info("************************************************************");

            List<Blog> list = new ArrayList<>();

            StopWatch sw = new StopWatch();
            sw.start();
            int blogCount = 10000000;
            for (int i = 0; i < blogCount; i++) {
                try {
                    Blog blog = Blog.builder().title("공부해야할 책4")
                            .content("재미집니다. 꼭 읽어보세요.")
                            .author("자바_" + i)
                            .delete(Boolean.FALSE)
                            .createdAt(LocalDateTime.now())
                            .baseEntity(BaseEntity.builder()
                                    .id("F_" + i)
                                    .createdBy("hoon")
                                    .createdDate(new Date())
                                    .updatedBy("hoon")
                                    .updatedDate(new Date())
                                    .version(1L)
                                    .build()
                            ).build();

                    //list.add(blog);

                    //blogRepository.save(blog).subscribe();
                    Mono<Blog> blogMono = blogRepository.save(blog);
                    log.info("blogMono saved : {}", blogMono.block().toString());

                    //Flux<Blog> blogFlux = blogRepository.findAll();
                    //log.info("blogFlux.count() : {}", blogFlux.count().block());

                    Thread.sleep(30);
                } catch (Exception e) {
                    log.error("error ", e);
                }
            }
            sw.stop();
            //log.info("{} 건 저장 total time seconds : {}", blogCount, sw.getTotalTimeSeconds());

            /*Flux<Blog> blogFlux = Flux.fromIterable(list).delayElements(Duration.ofMillis(500));
            Flux<Blog> savedBlog = blogRepository.saveAll(blogFlux);
            savedBlog.subscribe(b -> log.info("b : {}", b));*/
        };
    }
}

