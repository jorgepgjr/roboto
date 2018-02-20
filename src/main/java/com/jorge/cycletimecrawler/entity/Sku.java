package com.jorge.cycletimecrawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Image images;
    private List<Price> prices;

    public void addImage(String image){
        List<String> zooms = images.getZooms();

        if (CollectionUtils.isEmpty(images.getZooms())){
            zooms = new ArrayList<>();
        }

        zooms.add(image);
    }
}
