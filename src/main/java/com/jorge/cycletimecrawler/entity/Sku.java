package com.jorge.cycletimecrawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
public class Sku {

    private String sku;
    @JsonIgnore
    private List<String> images;
    private List<Price> prices;

    public void addImage(String image){
        images.add(image);
    }
}
