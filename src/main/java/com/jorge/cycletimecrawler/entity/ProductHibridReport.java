package com.jorge.cycletimecrawler.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@Document
public class ProductHibridReport {

    @Id
    private String id;
    private String sku;
    private boolean pureMarketplace;
    private String bestSeller;
    private List<Seller> sellers;
    private List<String> observacoes;
    private String store;
    @CreatedDate
    public Date createDate;

    public boolean isHibrid(){
        //Se tem mais de um Seller e não é puro Marketplace então é um Hibrido
        return CollectionUtils.isNotEmpty(sellers) && !pureMarketplace && sellers.size() > 1;
    }

    public List<String> addObservacao(String observacao) {
        if (observacoes == null){
            observacoes = new ArrayList<>();
        }
        observacoes.add(observacao);
        return observacoes;
    }
}
