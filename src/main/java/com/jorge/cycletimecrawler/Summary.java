package com.jorge.cycletimecrawler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by jorge on 17/08/17.
 */
@Data
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class Summary {

    private Issue issue;
    private String oldStatus;
    private String newStatus;
}
