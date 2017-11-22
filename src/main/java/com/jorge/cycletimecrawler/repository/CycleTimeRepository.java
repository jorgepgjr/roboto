package com.jorge.cycletimecrawler.repository;

import com.jorge.cycletimecrawler.entity.CycleTime;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jorge on 16/08/17.
 */
public interface CycleTimeRepository extends CrudRepository<CycleTime, String>{

    CycleTime findByIssueKey(String key);
}
