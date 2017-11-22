package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.Sku;
import org.apache.http.client.utils.URIBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.awt.print.Book;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jorge on 07/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImagesServiceTests {

    @Autowired
    private ImagesService imagesService;

    @Test
    public void imageTest() throws IOException, InterruptedException, URISyntaxException {
        List<Sku> skus = imagesService.getImagesATG(Arrays.asList("C62-0774-050-01"));

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

    private void writeBook(Sku sku, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(sku.getSku());

        final List<String> images = sku.getImages();

        for (int i = 0; i < images.size() ; i++) {
            cell = row.createCell(i+2);
            cell.setCellValue(images.get(i));
        }

    }

//    @Test
//    public void imageTeste() throws IOException, InterruptedException, URISyntaxException {
//
//        URIBuilder builder = new URIBuilder("//static4.netshoes.net/Produtos/medicine-ball-gonew-by-ziva-10-kg/50/C62-0774-050/C62-0774-050_zoom3.jpg?resize=544:*");
//
//    }

}
