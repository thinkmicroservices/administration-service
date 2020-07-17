package com.thinkmicroservices.ri.spring.admin;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

 
@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class AdministrationServiceApplication {
@Value("${configuration.source:DEFAULT}")
private String configSource;
@Value ("${spring.application.name:NOT-SET}")    
private String serviceName;
    public static void main(String[] args) {
        SpringApplication.run(AdministrationServiceApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate( ){
        return new RestTemplate();
    } 
    
     @PostConstruct
    private void displayInfo() {
         log.info("Service-Name:{}, configuration.source={}",serviceName,configSource);
    }
}
