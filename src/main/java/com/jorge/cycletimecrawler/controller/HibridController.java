package com.jorge.cycletimecrawler.controller;

import com.jorge.cycletimecrawler.service.HibridService;
import com.jorge.cycletimecrawler.service.ImagesService;
import com.jorge.cycletimecrawler.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jorge on 16/08/17.
 */
@Controller
public class HibridController {

    @Autowired
    private HibridService hibridService;

    @RequestMapping(value = "/checkHibrid", method = RequestMethod.POST)
    public ModelAndView checkHibrid(String skus) throws IOException, URISyntaxException, InterruptedException {
        if (skus != null ){
            List<String> list = Arrays.asList(skus.split("\\r\\n+"));
            hibridService.checkIfAvaible(list);
        }

        final ModelAndView modelAndView = new ModelAndView("redirect:/");
        return modelAndView;
    }

}
