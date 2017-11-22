package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.ProductHibridReport;
import com.jorge.cycletimecrawler.entity.Seller;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class HybridCrawlerService {


    @Async
    public CompletableFuture<ProductHibridReport> checkIfAvaibleFreedom(String sku) {
        ProductHibridReport hibrid = new ProductHibridReport();
        hibrid.setSku(sku);

        Document doc = null;
        try {
            System.out.println("Buscando SKU: " + sku + "...");
            doc = Jsoup.connect("http://www.netshoes.com.br/test/getMarketplaceInfoForSku.jsp?itemId=" + sku)
                    .validateTLSCertificates(false)
                    .method(Connection.Method.GET)
                    .timeout(10000)
                    .get();

            Elements isPureMkpElements = doc.getElementsMatchingOwnText("Is Pure Mkp");
            if (isPureMkpElements.size() == 0 ){
                System.out.println("Sku não encontrado: " + sku);
                hibrid.addObservacao("Sku não encontrado");
            } else {
                hibrid.setPureMarketplace(isPureMkpElements.get(0).html().matches(".*true"));

                hibrid.setBestSeller(doc.getElementsMatchingOwnText("Site Specific Best Sellers:").
                        get(0).parent().parent().siblingElements().get(0).child(1).html());

                Elements sellers = doc.getElementsByAttributeValue("style", "background-color:#FFFFCC;");

                if (sellers == null || sellers.size() == 0){
                    hibrid.addObservacao("Nenhum Seller encontrado");
                } else{
                    hibrid.setStore("Netshoes");
                    List<Seller> sellersReport = new ArrayList<>();
                    for (int i = 0; i < sellers.size(); i++) {
                        final Seller seller = new Seller();
                        seller.setSellerName(sellers.get(i).siblingElements().get(0).html());
                        seller.setStock(sellers.get(i).siblingElements().get(1).html());

                        sellersReport.add(seller);
                    }
                    hibrid.setSellers(sellersReport);
                }
                System.out.println("Gerado com sucesso o Sku: " + sku);
            }
        } catch (IOException e) {
            System.out.println("Erro sku: " + sku);
            System.out.println(e.getMessage());
        }

        return CompletableFuture.completedFuture(hibrid);
    }


}
