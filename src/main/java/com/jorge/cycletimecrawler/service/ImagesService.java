package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.ProductResponse;
import com.jorge.cycletimecrawler.entity.Sku;
import com.jorge.cycletimecrawler.integration.FreedomIntegration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Optional.empty;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class ImagesService {

    @Autowired
    FreedomIntegration freedomIntegration;

    @Autowired
    ImageCrawlerService imageCrawlerService;


    public List<Sku> getImagesFreedom(List<String> skus) throws InterruptedException {
        System.out.println("********************* Processo Iniciado ********************* ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date inicio = new Date();
        System.out.println(dateFormat.format(inicio));

        List<Sku> skusResponse = new ArrayList<>();

        for (String sku : skus) {
            ProductResponse response = freedomIntegration.getProductNS(sku);

            if (response != null && CollectionUtils.isNotEmpty(response.getSkus())) {
                skusResponse.add(response.getSkus().get(0));
            }else {
                ProductResponse responseZT = freedomIntegration.getProductZT(sku);
                if (responseZT != null && CollectionUtils.isNotEmpty(responseZT.getSkus())) {
                    skusResponse.add(responseZT.getSkus().get(0));
                }

            }
            Thread.sleep(10);
        }

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

        if (sku.getImages() != null && sku.getImages().getZooms() != null){
            final List<String> images = sku.getImages().getZooms();

            for (int i = 0; i < images.size(); i++) {
                cell = row.createCell(i + 2);
                cell.setCellValue("static.netshoes.com.br"+ images.get(i));
            }
        }

    }
}
