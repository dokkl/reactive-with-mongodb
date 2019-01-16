package com.hoon.project.reactivewithmongodb.repository;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by babybong on 08/01/2019.
 */
@Builder
@Data
public class BaseEntity implements Serializable {

    @Id
    private String id;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private Date updatedDate;

    @Version
    private Long version;



}
