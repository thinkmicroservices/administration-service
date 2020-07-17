/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.controller;

import io.swagger.annotations.ApiModelProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author cwoodward
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedTelemetryEventRequest {
       @ApiModelProperty(value = "Desired page.", position = 0, required = false)
    private Integer page;

    @ApiModelProperty(value = "Number of elements per page.", position = 1,required = false)
    private Integer pageSize;

    @ApiModelProperty(value = "Attribute to sort by.", position = 2,required = false)
    private String sortBy;

    @ApiModelProperty(value = "List of event types to include.", position = 3,required = false)
    private List<String> level;

    @ApiModelProperty(value = "Array of account ids to include.", position = 4,required = false)
    private String accountId;

    @ApiModelProperty(value = "Starting zoned date/timestamp.", position =5,required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startDate;

    @ApiModelProperty(value = "Ending zoned date/timestamp", position = 6,required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    
    private ZonedDateTime endDate;
}
