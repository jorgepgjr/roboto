package com.jorge.cycletimecrawler;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by jorge on 16/08/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Sprint {

    public int id;
    public int sequence;
    public String name;
}
