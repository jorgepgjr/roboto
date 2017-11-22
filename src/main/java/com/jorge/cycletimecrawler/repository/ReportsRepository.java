package com.jorge.cycletimecrawler.repository;

import com.jorge.cycletimecrawler.entity.Reports;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
public interface ReportsRepository extends CrudRepository<Reports, String>{

    List<Reports> findByOrderByCreateDateAsc();
    List<Reports> findByOrderByCreateDateDesc();
}
