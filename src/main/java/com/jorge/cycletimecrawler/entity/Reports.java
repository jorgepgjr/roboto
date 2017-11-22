package com.jorge.cycletimecrawler.entity;

import com.jorge.cycletimecrawler.Contents;
import com.jorge.cycletimecrawler.Sprint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by jorge on 16/08/17.
 */
@Document
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class Reports {

    @Id
    private String id;
    public Contents contents;
    public Sprint sprint;
    @CreatedDate
    public Date createDate;
    @LastModifiedDate
    public Date updateDate;
}
