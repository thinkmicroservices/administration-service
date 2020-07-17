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

 
 
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author cwoodward
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
 
public class JWTFilter implements Filter {

    /**
     * message returned when the supplied token has expired.
     */
    protected static final String TOKEN_EXPIRED_MESSAGE = "Token Expired";
    /**
     * message returned when the no token is present
     */
    protected static final String TOKEN_MISSING_MESSAGE = "Token Missing";
    @Autowired
    private JWTService jwtService;

    /**
     * filters incoming requests and ensures a token is provided and has not
     * expired. The token is added to the request prior to dispatch to the next
     * filter.
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        log.debug("invoking JWTFilter...");
        checkJWTServiceAvailable(request);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

         
        
        
        String authHeader = httpRequest.getHeader("Authorization");

        if ((authHeader != null) && (authHeader.length() > 7)) {
            String token = authHeader.substring(7);
            log.debug("uri:{},token=>{}", httpRequest.getRequestURI(),token);
            try {
                JWT jwt = jwtService.decodeJWT(token);
                // check if the token
                // hasn't expired
                if (jwt.isTokenExpired()) {
                       log.debug("uri:{},token expired", httpRequest.getRequestURI() );
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, TOKEN_EXPIRED_MESSAGE);
                    return;
                }
                request.setAttribute("JWT", jwt);
                log.debug("uri:{},set JWT Attribute", httpRequest.getRequestURI() );
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, TOKEN_MISSING_MESSAGE);
                return;
            }
        }

        chain.doFilter(request, response);

    }

     
     public void checkJWTServiceAvailable(ServletRequest request){
         log.debug("checking JWTService");
        // this is hack to get the jwtService in the Filter
          if(jwtService == null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            jwtService = webApplicationContext.getBean(JWTService.class);
          }
         log.debug("JWTService=>{}",jwtService);
     }
}
