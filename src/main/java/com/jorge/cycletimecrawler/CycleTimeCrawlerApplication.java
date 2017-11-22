package com.jorge.cycletimecrawler;

import com.jorge.cycletimecrawler.service.ImagesService;
import com.jorge.cycletimecrawler.service.ReportsService;
import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableMongoRepositories(basePackages = { "com.jorge.cycletimecrawler.repository" })
@EnableMongoAuditing
@EnableFeignClients(basePackages = "com.jorge.cycletimecrawler.integration")
@Configuration
@EnableAsync
public class CycleTimeCrawlerApplication {

	@Autowired
	private ReportsService reportsService;

	@Autowired
    private ImagesService imagesService;

	public static void main(String[] args) {
		SpringApplication.run(CycleTimeCrawlerApplication.class, args);
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(Integer.MAX_VALUE);
		executor.setThreadNamePrefix("ImageCrawler-");
		executor.initialize();
		return executor;
	}
//
//
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }
//
//	@Override
//	public void run(String... strings) throws Exception {
//        imagesService.getImagesATG(Arrays.asList("C62-0774-050-01"));
//	}
}
