package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.Image;
import com.jorge.cycletimecrawler.entity.Sku;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class ImageCrawlerService {


    @Async
    public CompletableFuture<Sku> getImages(String sku) {
        Sku product = new Sku(sku, new Image(), null);
        Document doc;
        try {
            System.out.println("Buscando SKU: " + sku + "...");
            doc = Jsoup.connect("http://www.netshoes.com.br/produto/" + sku)
                    .validateTLSCertificates(false)
                    .method(Connection.Method.GET)
                    .timeout(10000)
                    .get();

            Elements elements = doc.getElementsByClass("photo");
            if (elements == null || elements.size() == 0) {
                System.out.println("SKU: " + sku + "não encontrado");
            } else {
                Elements links = elements.get(0).getElementsByTag("img");
                for (Element link : links) {
                    final String image = link.attr("src");
                    final URIBuilder builder = new URIBuilder(image);

                    product.addImage(builder.getHost() + builder.getPath());
                }

            }
        } catch (IOException e) {
            System.out.println("Erro sku: " + sku);
            System.out.println(e.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("Erro parsear URL sku: " + sku);
            e.printStackTrace();
        }
        System.out.println("Gerado com sucesso o Sku: " + sku);
        return CompletableFuture.completedFuture(product);
    }
}
