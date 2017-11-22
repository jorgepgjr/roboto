package com.jorge.cycletimecrawler.repository;

import com.jorge.cycletimecrawler.entity.ProductHibridReport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
public interface HibridRepository extends CrudRepository<ProductHibridReport, String>{

    List<ProductHibridReport> findByOrderByCreateDateAsc();
    List<ProductHibridReport> findByOrderByCreateDateDesc();
}
