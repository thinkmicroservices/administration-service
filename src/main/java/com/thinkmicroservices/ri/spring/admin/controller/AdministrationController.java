 
package com.thinkmicroservices.ri.spring.admin.controller;

import com.thinkmicroservices.ri.spring.admin.jwt.JWT;
import com.thinkmicroservices.ri.spring.admin.jwt.JWTService;
import com.thinkmicroservices.ri.spring.admin.model.Profile;
import com.thinkmicroservices.ri.spring.admin.service.DiscoveryService;
import com.thinkmicroservices.ri.spring.admin.service.RestResponsePage;

import com.thinkmicroservices.ri.spring.admin.service.UserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import static org.reflections.Reflections.collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cwoodward
 */
@Api(value = "Administration Management System", description = "Operations pertaining to application-level administraion")
@Slf4j
//@CrossOrigin(origins = {"${controller.cors.origin}"}, allowedHeaders = "*")

@RestController
public class AdministrationController {

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private DiscoveryService discoveryService;

    @Autowired
    private JWTService jwtService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<RestResponsePage<Profile>> getUserProfiles(@ApiParam("The page number to render.") @RequestParam(defaultValue = "0") int pageNo,
            @ApiParam("<Optional> The number of items per page.") @RequestParam(defaultValue = "10") int pageSize,
            @ApiParam("<Optional> field to sort by.") @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam("<Optional> Regex to filter (applied to first,middle and last name fields)- default to all /./") @RequestParam(defaultValue = ".") String like, HttpServletRequest httpServletRequest) {
        log.debug("get user profiles");

       // JWT jwt = jwtService.getJWT(httpServletRequest);
       // if (jwt == null) {
       //     return new ResponseEntity<RestResponsePage<Profile>>(HttpStatus.UNAUTHORIZED);
       // }
      //  if (jwt.hasRole(JWT.ROLE_ADMIN)) {
            RestResponsePage<Profile> userProfiles = userAdminService.findUserProfilesByPage(pageNo, pageSize, sortBy, like, httpServletRequest.getHeader("Authorization"));

            return new ResponseEntity<>(userProfiles, HttpStatus.OK);
      //  }
       // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/setUserActiveStatus/{accountId}", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                required = true, dataType = "string", paramType = "header")})

    public ResponseEntity<?> setUserActiveStatus(@PathVariable String accountId,
            @RequestParam boolean activeStatus, HttpServletRequest httpServletRequest) {

        String authHeader = httpServletRequest.getHeader("Authorization");
        log.debug("get user profiles");
       // JWT jwt = jwtService.getJWT(httpServletRequest);

      //  if (jwt == null) {
         //   return new ResponseEntity<RestResponsePage<Profile>>(HttpStatus.UNAUTHORIZED);
       // }

      //  if (jwt.hasRole(JWT.ROLE_ADMIN)) {
            return ResponseEntity.ok(this.userAdminService.setUserActiveStatus(accountId, activeStatus, authHeader));
       // }
       // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/getServiceInstances", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<?> getServiceInstanceList(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(this.discoveryService.getAllServiceInstances());
    }

    @PostMapping(path = "/getTelemetryByAccountId")
    @ResponseBody
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<PagedTelemetryEventResponse<ClientTelemetryEvent>  > findByAccountId(@RequestBody PagedTelemetryEventRequest request, HttpServletRequest httpServletRequest) {

        //JWT jwt = jwtService.getJWT(httpServletRequest);
        //if (jwt == null) {
        //    return new ResponseEntity<RestResponsePage<Profile>>(HttpStatus.UNAUTHORIZED);
       // }
       // if (jwt.hasRole(JWT.ROLE_ADMIN)) {
            PagedTelemetryEventResponse<ClientTelemetryEvent> pagedResult = userAdminService.findTelemetryByPage(request, httpServletRequest.getHeader("Authorization"));
            
           
        
         
        
        return ResponseEntity.ok( pagedResult);
       // }
       // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
