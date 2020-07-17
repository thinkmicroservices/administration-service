/*
 * Copyright 2019 cwoodward.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thinkmicroservices.ri.spring.admin.jwt;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Builder;

/**
 *
 * @author cwoodward
 */
@Data
@Builder
public class JWT {
    
    public static final String ROLE_USER="user";
    public static final String ROLE_ADMIN="admin";
    
    private String subject;
    private String accountId;
    private Date issuedAt;
    private Date expiresAt;
    private List<String> roles;
    
    // helper methods
    public boolean isTokenExpired(){
         
        if(System.currentTimeMillis()>expiresAt.getTime()){
            return true;
        }
        return false;
    }
    
    public boolean hasRole(String role){
        return roles.contains(role);
    }
    
    public boolean hasAccountId(){
        if(accountId== null){
            return false;
        }
        return true;
    }
}
