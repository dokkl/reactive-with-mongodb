package com.hoon.project.reactivewithmongodb.repository;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Delegate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by babybong on 08/01/2019.
 */
@Builder
@Data
@Document(collection = "blog")
public class Blog {
    @Delegate
    private BaseEntity baseEntity;

    @TextIndexed
    private String title;

    private String content;

    @Indexed
    private String author;

    private Boolean delete;

    @Indexed
    @CreatedDate
    private LocalDateTime createdAt;
}
