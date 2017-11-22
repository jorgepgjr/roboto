package com.jorge.cycletimecrawler.entity;

import com.jorge.cycletimecrawler.Issue;
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
public class CycleTime {

    @Id
    private String id;
    private Issue issue;
    private Date startDate;
    private Date doneDate;
    private int cycleTime;
    @CreatedDate
    public Date createDate;
    @LastModifiedDate
    public Date updateDate;
}
