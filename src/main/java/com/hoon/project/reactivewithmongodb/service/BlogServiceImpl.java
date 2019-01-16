package com.hoon.project.reactivewithmongodb.service;

import com.hoon.project.reactivewithmongodb.repository.Blog;
import com.hoon.project.reactivewithmongodb.repository.primary.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by babybong on 08/01/2019.
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Mono<Blog> createBlog(Blog blog) {
        return blogRepository.insert(blog);
    }

    @Override
    public Flux<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Mono<Blog> updateBlog(Blog blog, String id) {
        return findOne(id).doOnSuccess(findBlog -> {
            findBlog.setContent(blog.getContent());
            findBlog.setTitle(blog.getTitle());
            findBlog.setAuthor(blog.getAuthor());
            blogRepository.save(findBlog).subscribe();
        });
    }

    @Override
    public Mono<Blog> findOne(String id) {
        return blogRepository.findByIdAndDeleteIsFalse(id).
                switchIfEmpty(Mono.error(new Exception("No Blog found with Id: " + id)));
    }

    @Override
    public Flux<Blog> findByTitle(String title) {
        return blogRepository.findByAuthorAndDeleteIsFalse(title)
                .switchIfEmpty(Mono.error(new Exception("No Blog found with title Containing : " + title)));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return findOne(id).doOnSuccess(blog -> {
            blog.setDelete(true);
            blogRepository.save(blog).subscribe();
        }).flatMap(blog -> Mono.just(Boolean.TRUE));
    }
}
