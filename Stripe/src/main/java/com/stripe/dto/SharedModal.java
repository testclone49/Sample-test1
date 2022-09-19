/**
 * Created by: nuwan_r
 * Created on: 4/21/2021
 **/
package com.stripe.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

public class SharedModal implements Serializable {

    @CreatedDate
    @Getter
    @Setter
    private Date createdDate;

    @LastModifiedDate
    @Getter
    @Setter
    private Date lastModifiedDate;
}
