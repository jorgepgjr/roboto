package com.jorge.cycletimecrawler.controller;

import com.jorge.cycletimecrawler.service.ImagesService;
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
public class ImagesController {

    @Autowired
    private ImagesService imagesService;

    @RequestMapping(value = "/getImages", method = RequestMethod.POST)
    public ModelAndView checkOnJira(String skus) throws IOException, URISyntaxException, InterruptedException {
        if (skus != null ){
            List<String> list = Arrays.asList(skus.split("\\r\\n+"));
            imagesService.getImagesATG(list);
        }

        final ModelAndView modelAndView = new ModelAndView("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "/getImagesFreedom", method = RequestMethod.POST)
    public ModelAndView getImagesFreedom(String skus) throws IOException, URISyntaxException, InterruptedException {
        if (skus != null ){
            List<String> list = Arrays.asList(skus.split("\\r\\n+"));
            imagesService.getImagesFreedom(list);
        }

        final ModelAndView modelAndView = new ModelAndView("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() throws IOException {
        final ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

}
