package com.hoon.project.reactivewithmongodb.service;

import com.hoon.project.reactivewithmongodb.repository.Blog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by babybong on 08/01/2019.
 */
public interface BlogService {
    Mono<Blog> createBlog(Blog blog);

    Mono<Blog> updateBlog(Blog blog, String id);

    Flux<Blog> findAll();

    Mono<Blog> findOne(String id);

    Flux<Blog> findByTitle(String title);

    Mono<Boolean> delete(String id);
}
