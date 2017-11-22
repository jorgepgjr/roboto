package com.jorge.cycletimecrawler.controller;

import com.jorge.cycletimecrawler.Issue;
import com.jorge.cycletimecrawler.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jorge on 16/08/17.
 */
@Controller
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() throws IOException {
        final Map<String, List<Issue>> reports = reportsService.getReports(false);


        final ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("news", reports.get("new"));
        modelAndView.addObject("wip", reports.get("wip"));
        modelAndView.addObject("done", reports.get("done"));
        return modelAndView;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ModelAndView test() {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save() throws IOException {
        final Map<String, List<Issue>> reports = reportsService.getReports(true);
        final ModelAndView modelAndView = new ModelAndView("redirect:/");

        modelAndView.addObject("news", reports.get("new"));
        modelAndView.addObject("wip", reports.get("wip"));
        modelAndView.addObject("done", reports.get("done"));
        return modelAndView;
    }

    @RequestMapping(value = "/checkOnJira", method = RequestMethod.POST)
    public ModelAndView checkOnJira(String jiras) throws IOException {
        if (jiras != null ){
            List<String> list = Arrays.asList(jiras.split("\\r\\n+"));
        }



        final Map<String, List<Issue>> reports = reportsService.getReports(false);
        final ModelAndView modelAndView = new ModelAndView("redirect:/");

        modelAndView.addObject("news", reports.get("new"));
        modelAndView.addObject("wip", reports.get("wip"));
        modelAndView.addObject("done", reports.get("done"));
        return modelAndView;
    }

}
