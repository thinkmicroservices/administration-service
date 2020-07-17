/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.service;

import java.util.List;
import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;

/**
 *
 * @author cwoodward
 */
@Data
public class NamedServiceInstance {
    
    private String serviceName;
    private List<ServiceInstance> instances;

    NamedServiceInstance(String serviceName, List<ServiceInstance> instances) {
        this.serviceName=serviceName;
        this.instances=instances;
    }
}
