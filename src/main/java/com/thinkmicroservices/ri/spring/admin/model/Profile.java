/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.model;

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
 
public class Profile {

    
    private String id;
    private String accountId;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    
    private String primaryStreetAddress;
    private String secondaryStreetAddress;
    private String city;
    private String state;
    private String postalCode;
    
    private String dob;
    private boolean activeStatus;
     
    
    
           
}
