package com.jorge.cycletimecrawler.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProductResponse {

    private List<Sku> skus;

}
