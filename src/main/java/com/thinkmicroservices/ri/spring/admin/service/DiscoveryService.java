
package com.thinkmicroservices.ri.spring.admin.service;

import java.util.ArrayList;
 
import java.util.List;
 
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

/**
 *
 * @author cwoodward
 */
@Service
@Slf4j
public class DiscoveryService {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    
    
    
    public List<NamedServiceInstance> getAllServiceInstances( ){
         List<String> services= discoveryClient.getServices().stream().sorted().collect(Collectors.toList());
         List<NamedServiceInstance> allServices= new ArrayList<>();
        for(String service: services){
             List<ServiceInstance> instances = discoveryClient.getInstances(service);
             if(instances!= null){
                 allServices.add(new NamedServiceInstance(service,instances));
             }
        }
        return allServices;
    }
}
