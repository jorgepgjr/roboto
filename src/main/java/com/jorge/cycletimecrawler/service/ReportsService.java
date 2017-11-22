package com.jorge.cycletimecrawler.service;

import com.jorge.cycletimecrawler.Issue;
import com.jorge.cycletimecrawler.Summary;
import com.jorge.cycletimecrawler.entity.CycleTime;
import com.jorge.cycletimecrawler.entity.Reports;
import com.jorge.cycletimecrawler.repository.CycleTimeRepository;
import com.jorge.cycletimecrawler.repository.ReportsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jorge on 16/08/17.
 */
@Component
public class ReportsService {

    @Autowired
    private ReportsRepository reportsRepository;

    @Autowired
    private CycleTimeRepository cycleTimeRepository;

    private List<Summary> summaries = new ArrayList<>();
    private List<Issue> newIssues = new ArrayList<>();
    private List<Issue> issuesDone = new ArrayList<>();
    private List<Issue> issuesWIP = new ArrayList<>();
    private List<Issue> completedIssuesOnJira = new ArrayList<>();
    private List<Issue> completedIssuesOnDB = new ArrayList<>();
    private List<Issue> incompletedIssuesOnJira = new ArrayList<>();
    private List<Issue> incompletedIssuesOnDB = new ArrayList<>();

    public void resetElements(){
        summaries = new ArrayList<>();
        newIssues = new ArrayList<>();
        issuesDone = new ArrayList<>();
        issuesWIP = new ArrayList<>();
        completedIssuesOnJira = new ArrayList<>();
        completedIssuesOnDB = new ArrayList<>();
        incompletedIssuesOnJira = new ArrayList<>();
        incompletedIssuesOnDB = new ArrayList<>();
    }
    public Map<String, List<Issue>> getReports(boolean salvar) throws IOException {
        resetElements();

        final Reports reports = getReportsOnJira();
        final Reports lastReport = reportsRepository.findByOrderByCreateDateDesc().get(0);
        compare(reports, lastReport);
        if (salvar){
            reportsRepository.save(reports);
        }

        final HashMap<String, List<Issue>> result = new HashMap<>();
        result.put("new", newIssues);
        result.put("done", issuesDone);
        result.put("wip", issuesWIP);
        return result;
    }

    private Reports getReportsOnJira() throws IOException {
        Connection.Response res = Jsoup.connect("https://jira.ns2online.com.br/login.jsp")
                .data("os_username", "jorge.peres")
                .data("os_password", "Abril0128")
                .validateTLSCertificates(false)
                .method(Connection.Method.POST)
                .timeout(10000)
                .execute();

        Map<String, String> cookies = res.cookies();

        String report = Jsoup.connect("https://jira.ns2online.com.br/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId=2235&sprintId=4752")
                .cookies(cookies)
                .validateTLSCertificates(false)
                .ignoreContentType(true)
                .timeout(10000)
                .execute()
                .header("Accept", "text/javascript")
                .body();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(report, Reports.class);
    }

    private void compare(Reports reports, Reports lastReport) {
        completedIssuesOnJira = reports.getContents().getCompletedIssues();
        completedIssuesOnDB = lastReport.getContents().getCompletedIssues();
        incompletedIssuesOnJira = reports.getContents().getIncompletedIssues();
        incompletedIssuesOnDB = lastReport.getContents().getIncompletedIssues();

        completedIssuesOnJira.stream().forEach(completed -> {
            summary(completed);
        });

        incompletedIssuesOnJira.stream().forEach(incompleted -> {
            summary(incompleted);
        });
    }

    private void summary(Issue issueOnJira) {
        List<Issue> foundOnDB = new ArrayList<>();
        List<Issue> foundCompleted = new ArrayList<>();
        List<Issue> foundIncompleted = new ArrayList<>();

        completedIssuesOnDB.stream()
                .filter(issueOnJira :: equals)
                .findFirst()
                .ifPresent(foundCompleted :: add);

        incompletedIssuesOnDB.stream()
                .filter(issueOnJira :: equals)
                .findFirst()
                .ifPresent(foundIncompleted :: add);

        foundOnDB.addAll(foundCompleted);
        foundOnDB.addAll(foundIncompleted);

        if (!CollectionUtils.isEmpty(foundOnDB) && (isInWIP(issueOnJira) && !isInWIP(foundOnDB.get(0)))) {
            issuesWIP.add(issueOnJira);
        }

        if (CollectionUtils.isEmpty(foundOnDB)) {
            newIssues.add(issueOnJira);

        } else if (isDone(issueOnJira) && !isDone(foundOnDB.get(0))) {
            issuesDone.add(issueOnJira);
        }
    }

    private boolean isDone(Issue issue) {
        List<String> statusDone = new ArrayList<String>() {{
            add("Em Produção");
            add("Cancelado");
            add("Arquivado");
        }};
        return statusDone.contains(issue.getStatusName());
    }

    private boolean isInWIP(Issue issue) {
        List<String> statusWIP = new ArrayList<String>() {{
            add("Em Desenvolvimento");
            add("Pronto para Testes");
            add("Aguardando Review");
            add("Em Testes Regressivos");
            add("Aguardando Release Train");

        }};
        return statusWIP.contains(issue.getStatusName());
    }

}
