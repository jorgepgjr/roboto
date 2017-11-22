package com.jorge.cycletimecrawler;


import com.jorge.cycletimecrawler.entity.Reports;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;


public class ReportCreator {

    @Test
    public void test() throws IOException {
        Response res = Jsoup.connect("https://jira.ns2online.com.br/login.jsp")
                .data("os_username", "jorge.peres")
                .data("os_password", "PASSWORD-AQUI")
                .validateTLSCertificates(false)
                .method(Connection.Method.POST)
                .execute();

        Map<String, String> cookies = res.cookies();

        String report = Jsoup.connect("https://jira.ns2online.com.br/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=2235&sprintId=4259")
                .cookies(cookies)
                .validateTLSCertificates(false)
                .ignoreContentType(true)
                .execute()
                .header("Accept", "text/javascript")
                .body();



        ObjectMapper mapper = new ObjectMapper();
        Reports user = mapper.readValue(report, Reports.class);

    }
}
