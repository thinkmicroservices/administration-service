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
package com.thinkmicroservices.ri.spring.admin;


 
import com.thinkmicroservices.ri.spring.admin.jwt.JWTFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author cwoodward
 */
@Configuration
@Slf4j
public class FilterConfig {

    
    protected static final String URL_PATTERN= "/*";

     
 
    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JWTFilter> filterRegistrationBean
                = new FilterRegistrationBean<>(new JWTFilter());

        filterRegistrationBean.addUrlPatterns(URL_PATTERN);
         
        log.debug("JWTFilter patterns {}", filterRegistrationBean.getUrlPatterns());
        return filterRegistrationBean;
    }

}
