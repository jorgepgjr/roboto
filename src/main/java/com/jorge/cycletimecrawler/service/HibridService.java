package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.ProductHibridReport;
import com.jorge.cycletimecrawler.entity.ProductResponse;
import com.jorge.cycletimecrawler.entity.Seller;
import com.jorge.cycletimecrawler.entity.Sku;
import com.jorge.cycletimecrawler.integration.FreedomIntegration;
import com.jorge.cycletimecrawler.repository.HibridRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class HibridService {

    @Autowired
    private FreedomIntegration freedomIntegration;

    @Autowired
    private HybridCrawlerService hybridCrawlerService;

    @Autowired
    private HibridRepository hibridRepository;


    public List<ProductHibridReport> checkIfAvaibleFreedom(List<String> skus) throws InterruptedException {
        final Date inicio = inicioProcesso();

        final List<ProductHibridReport> hibridReports = new ArrayList<>();

        for (String sku : skus) {
            System.out.println("Buscando SKU: " + sku + "...");
            ProductResponse response = freedomIntegration.getProduct(sku);
            final ProductHibridReport hibridReport = new ProductHibridReport();
            hibridReport.setSku(sku);

            if (response != null && CollectionUtils.isNotEmpty(response.getSkus())) {
                final Sku skuResponse = response.getSkus().get(0);
                hibridReport.setStore("Zattini");
                final List<Seller> sellers = new ArrayList<>();
                skuResponse.getPrices().forEach(price -> {
                    sellers.add(new Seller(price.getSeller().getId(), price.getSeller().getName()));
                });
                hibridReport.setSellers(sellers);
            }else {
                hibridReport.addObservacao("Produto não encontrando na Zattini");
            }
            System.out.println("Gerado com sucesso o Sku: " + sku);
            hibridReports.add(hibridReport);
            hibridRepository.save(hibridReport);
        }
        try{
            fimProcesso(inicio);
            System.out.println("Gerado XLS...");
            generateXLS(hibridReports, "Zattini-Hibrid");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hibridReports;
    }

    public List<ProductHibridReport> checkIfAvaible(List<String> skus) {
        final Date inicio = inicioProcesso();
        List<ProductHibridReport> hibridReports = new ArrayList<>();

        for (String sku : skus) {
            CompletableFuture<ProductHibridReport> futureHybrid = hybridCrawlerService.checkIfAvaibleFreedom(sku);

            try {
                final ProductHibridReport hibridReport = futureHybrid.get();
                hibridRepository.save(hibridReport);
                hibridReports.add(hibridReport);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

//        try {
            fimProcesso(inicio);
            System.out.println("Gerando XLS...");
//            generateXLS(hibridReports, "Netshoes-Hibrid");
//        } catch (IOException e) {
//            System.out.println("Erro ao gerar XLS");
//        }
        return hibridReports;
    }

    private void generateXLS(List<ProductHibridReport> hibridReports, String filename) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();


        int rowCount = 1;
        //Titulo
        Row titulo = sheet.createRow(0);

        Cell cell = titulo.createCell(1);
        cell.setCellValue("SKU");

        Cell cell2 = titulo.createCell(2);
        cell2.setCellValue("Observações");

        Cell cell3 = titulo.createCell(3);
        cell3.setCellValue("Puro Marketplace?");

        Cell cell4 = titulo.createCell(4);
        cell4.setCellValue("Hibrido? (Netshoes + algum Seller)");

        for (ProductHibridReport hibridReport : hibridReports) {
            Row row = sheet.createRow(++rowCount);
            writeBook(hibridReport, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filename + ".xls")) {
            workbook.write(outputStream);
        }
    }

    private void writeBook(ProductHibridReport hibridReports, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(hibridReports.getSku());

        Cell cell2 = row.createCell(2);
        cell2.setCellValue(StringUtils.join(hibridReports.getObservacoes(), ", "));

        Cell cell3 = row.createCell(3);
        cell3.setCellValue(hibridReports.isPureMarketplace() ? "Sim" : "Não");

        Cell cell4 = row.createCell(4);
        cell4.setCellValue(hibridReports.isHibrid() ? "Sim" : "Não");


        List<Seller> sellers = hibridReports.getSellers();
        if (CollectionUtils.isNotEmpty(sellers)){
            for (int i = 0; i < sellers.size(); i++) {
                cell = row.createCell(i + 5);
                cell.setCellValue(sellers.get(i).getSellerName() +" " + sellers.get(i).getStock());
            }
        }

    }

    private void fimProcesso(Date inicio) {
        System.out.println("********************* Fim do Processo ********************* ");
        long totalTime = new Date().getTime() - inicio.getTime();
        System.out.println("O Processamentou levou " + TimeUnit.MILLISECONDS.toMinutes(totalTime) + "Minutos");
        System.out.println("O Processamentou levou " + TimeUnit.MILLISECONDS.toSeconds(totalTime) + "Segundos");
    }

    private Date inicioProcesso() {
        System.out.println("********************* Processo Iniciado ********************* ");
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date inicio = new Date();
        System.out.println(dateFormat.format(inicio));
        return inicio;
    }
}
