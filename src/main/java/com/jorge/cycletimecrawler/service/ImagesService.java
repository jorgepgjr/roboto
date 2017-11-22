package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.ProductResponse;
import com.jorge.cycletimecrawler.entity.Sku;
import com.jorge.cycletimecrawler.integration.FreedomIntegration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class ImagesService {

    @Autowired
    FreedomIntegration freedomIntegration;

    @Autowired
    ImageCrawlerService imageCrawlerService;

//
//    public List<Sku> getImagesFreedom(List<String> skus) throws InterruptedException {
//
//        List<Sku> skusResponse = new ArrayList<>();
//
//        for (String sku : skus) {
//            List<ProductResponse> response = freedomIntegration.getProduct(sku);
//
//            if (CollectionUtils.isNotEmpty(response)) {
//                skusResponse.add(response.get(0).getSkus().get(0));
//            }
//            Thread.sleep(100);
//        }
//
//        return skusResponse;
//
//    }

    public List<Sku> getImagesATG(List<String> skus) {
        System.out.println("********************* Processo Iniciado ********************* ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date inicio = new Date();
        System.out.println(dateFormat.format(inicio));

        System.out.println(new Date());
        List<Sku> skusResponse = new ArrayList<>();
        List<CompletableFuture> futureSKUs = new ArrayList<>();

        for (String sku : skus) {
            CompletableFuture<Sku> futureSku = imageCrawlerService.getImages(sku);
            futureSKUs.add(futureSku);
        }

        futureSKUs.forEach(result -> {
            try {
                skusResponse.add( (Sku) result.get() );
            } catch (InterruptedException | ExecutionException e) {
                //handle thread error
            }
        });

        System.out.println("********************* Fim do Processo ********************* ");
        final Date fim = new Date();
        System.out.println(dateFormat.format(fim));

        try {
            generateXLS(skusResponse);
        } catch (IOException e) {
            System.out.println("Erro ao gerar XLS");
        }
        return skusResponse;
    }

    private Sku getImages(String sku) {
        Sku product = new Sku(sku, new ArrayList<>(), null);
        Document doc;
        try {
            System.out.println("Buscando SKU: " + sku + "...");
            doc = Jsoup.connect("http://www.netshoes.com.br/produto/" + sku)
                    .validateTLSCertificates(false)
                    .method(Connection.Method.GET)
                    .timeout(10000)
                    .get();

            Elements elements = doc.getElementsByClass("photo-gallery-list");
            if (elements == null || elements.size() == 0) {
                System.out.println("SKU: " + sku + "não encontrado");
            } else {
                Elements links = elements.get(0).getElementsByTag("a");
                for (Element link : links) {
                    final String image = link.attr("data-large");
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

        return product;
    }

    private void generateXLS(List<Sku> skus) throws IOException {
        System.out.println("Gerando XLS...");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rowCount = 0;

        for (Sku sku : skus) {
            Row row = sheet.createRow(++rowCount);
            writeBook(sku, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream("imagesSku.xls")) {
            workbook.write(outputStream);
            System.out.println("XLS Gerado com sucesso");
        }
    }

    private void writeBook(Sku sku, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(sku.getSku());

        final List<String> images = sku.getImages();

        for (int i = 0; i < images.size(); i++) {
            cell = row.createCell(i + 2);
            cell.setCellValue(images.get(i));
        }

    }
}
