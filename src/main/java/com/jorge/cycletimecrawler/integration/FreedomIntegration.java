package com.jorge.cycletimecrawler.integration;

import com.jorge.cycletimecrawler.entity.ProductResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by jorge on 06/11/17.
 */

@FeignClient(url = "https://az-br-prd-free-gateway.netshoes.io/freedomcatalog", name = "freedomIntegration")
public interface FreedomIntegration {

    @RequestMapping(method = GET, value = "/skus/", params = "sku", headers = "storeid=L_ZATTINI")
    ProductResponse getProduct(@RequestParam(value = "sku") String sku);
}
