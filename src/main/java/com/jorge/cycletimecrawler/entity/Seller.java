package com.jorge.cycletimecrawler.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jorge on 19/11/17.
 */
@Data
@NoArgsConstructor
public class Seller {

    private String sellerId;
    private String sellerName;
    private String price;
    private String stock;

    public Seller(String sellerId, String sellerName) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }
}
