/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.controller;
 
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author cwoodward
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedTelemetryEventResponse<T> {
     private boolean resultsAvailable=false;
    private List<T> resultList;
    private long totalElements;
    private long totalPages;
    private long currentPage;
    private long currentPageElementNumber;
  
}
