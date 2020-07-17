package com.thinkmicroservices.ri.spring.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

 
import lombok.*;

@Data
@EqualsAndHashCode(exclude = "roles")
 

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  
    private long id;
 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
  
    private String accountId;
   
    private String username;

   
    private String email;

    
    @JsonIgnore
    private String password;

    
    @JsonIgnore
    private String recoveryCode;

    
    private boolean activeStatus;
  
    @JsonIgnore
    private java.sql.Timestamp recoveryExpires;

    @JsonIgnore
    private java.sql.Timestamp lastLogon;
    
    @JsonIgnore
    private String refreshToken;
 
    @JsonIgnore
    private java.sql.Timestamp refreshTokenExpiration;
}
