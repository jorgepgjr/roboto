package com.jorge.cycletimecrawler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by jorge on 15/08/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@EqualsAndHashCode(of = {"key"})
@ToString(includeFieldNames = false, of = {"key", "summary", "typeName"})
public class Issue {

    private String key;
    private String summary;
    private String statusName;
    private String typeName;
    @JsonProperty(value = "columnStatistic.statFieldValue.value")
    private String points;
    private boolean done;
}
