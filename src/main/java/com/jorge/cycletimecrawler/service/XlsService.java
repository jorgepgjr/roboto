package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.ProductResponse;
import com.jorge.cycletimecrawler.entity.Sku;
import com.jorge.cycletimecrawler.integration.FreedomIntegration;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class XlsService {

    public void generateXLS(List<Sku> skus) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rowCount = 0;

        for (Sku sku : skus) {
            Row row = sheet.createRow(++rowCount);
            writeBook(sku, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream("imagesSku.xls")) {
            workbook.write(outputStream);
        }
    }

    public void writeBook(Sku sku, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(sku.getSku());

        final List<String> images = sku.getImages();

        for (int i = 0; i < images.size(); i++) {
            cell = row.createCell(i + 2);
            cell.setCellValue(images.get(i));
        }

    }
}
