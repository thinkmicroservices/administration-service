/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.controller;

import java.time.ZonedDateTime;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
 
 

/**
 *
 * @author cwoodward
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
 

public class ClientTelemetryEvent {

    @Id
    private @NonNull  String id;
  
    private ZonedDateTime timestamp;
    private String source;
    private String accountId;
    private String level;
    private String message;
    private String[] details;
}
