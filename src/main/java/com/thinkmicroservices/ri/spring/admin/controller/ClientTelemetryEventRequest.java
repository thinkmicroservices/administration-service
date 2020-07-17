/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.controller;

import java.util.Date;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author cwoodward
 */
@Slf4j
@Data
public class ClientTelemetryEventRequest {
   private Date timestamp;
   private String accountId;
   private String level;
   private String message;
   private String[] details;
}
