package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.entity.ProductHibridReport;
import com.jorge.cycletimecrawler.entity.Sku;
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

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jorge on 07/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HibridServiceTests {

    @Autowired
    private HibridService hibridService;

    @Test
    public void checkIfAvaibleATG() throws IOException, InterruptedException, URISyntaxException {
        List<String> skus = readCsv();
        List<ProductHibridReport> skusResponse = hibridService.checkIfAvaible(skus);
    }

    @Test
    public void checkIfAvaibleFreedom() throws IOException, InterruptedException, URISyntaxException {
        List<String> skus = readCsv();
        List<ProductHibridReport> skusResponse = hibridService.checkIfAvaibleFreedom(skus);

    }


    private List<String> readCsv() {
        String csvFile = "/home/jorge/Documents/Hibridos/SKUs-Zattini-Restantes-sem.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<String> skus = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                skus.add(line.split(cvsSplitBy)[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return skus;
    }
}
