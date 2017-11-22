package com.jorge.cycletimecrawler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by jorge on 15/08/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
public class Contents {

    public List<Issue> completedIssues;
    public List<Issue> incompletedIssues;

}
