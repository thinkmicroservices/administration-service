/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.admin.service;

import com.thinkmicroservices.ri.spring.admin.controller.ClientTelemetryEvent;
import com.thinkmicroservices.ri.spring.admin.controller.PagedTelemetryEventRequest;
import com.thinkmicroservices.ri.spring.admin.controller.PagedTelemetryEventResponse;
import org.springframework.stereotype.Service;
import com.thinkmicroservices.ri.spring.admin.model.Profile;
import com.thinkmicroservices.ri.spring.admin.model.User;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author cwoodward
 */
@Service
@Slf4j
public class UserAdminService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private MeterRegistry meterRegistry;

    @Value("${authentication.service.endpoint.url:http://AZN-SERVICE}")
    private String authServiceEndpointURL;

    @Value("${profile.service.endpoint.url:http://ACCOUNT-PROFILE-SERVICE}")
    private String profileServiceEndpointURL;

    @Value("${telemetry.service.endpoint.url:http://TELEMETRY-SERVICE}")
    private String telemetryServiceEndpointURL;
    private Counter administrationUserStatusEnabled;
    private Counter administrationUserStatusDisabled;
    private Counter administrationFindUsers;
    private Counter administrationFindTelemetry;
 

    // this method enriches the paged profile models with the activeStatus
    // from the authorization service. It first retrieves the requested
    // page of user profiles from the profile service, accumulates the  
    // account ids and retrieves the corresponding user models. 
    public RestResponsePage<Profile> findUserProfilesByPage(int pageNo, int pageSize, String sortBy, String like,
            String authorizationHeader) {

        String profileURL = profileServiceEndpointURL + "/profile/all";

        log.debug("profile url {}", profileURL);
        HttpHeaders headers = new HttpHeaders();

        // propagate the auth token
        headers.add("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        // Query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(profileURL)
                // Add query parameter
                .queryParam("pageNo", pageNo)
                .queryParam("pageSize", pageSize)
                .queryParam("sortBy", sortBy)
                .queryParam("like", like);

        String urlWithQueryParams = builder.toUriString();
        log.debug("urlWithQueryParams {}", urlWithQueryParams);

        // get the request page of profiles....
        ResponseEntity<RestResponsePage<Profile>> response = restTemplate.exchange(urlWithQueryParams, HttpMethod.GET, entity, new ParameterizedTypeReference<RestResponsePage<Profile>>() {
        });

        log.debug("profile response={}", response);

        // generate a list of accountId strings from the retrieved page of profiles
        List<String> accountIdList = response.getBody().getContent().stream().map(Profile::getAccountId).collect(Collectors.toList());

        // call the authentication service to get the user models with the matching account ids
        log.debug("accountIdList={}", accountIdList);
        ///
        String authURL = authServiceEndpointURL + "/getAccountStatusByAccountIds";

        log.debug("authServiceEndpointURL={}", authServiceEndpointURL);
        HttpHeaders authHeaders = new HttpHeaders();

        // propagate the auth token
        authHeaders.add("Authorization", authorizationHeader);

        HttpEntity<List> entity2 = new HttpEntity<List>(accountIdList, authHeaders);
        log.debug("entity2={}", entity2);

        ResponseEntity<List<User>> usersResponse = restTemplate.exchange(authURL, HttpMethod.POST, entity2, new ParameterizedTypeReference<List<User>>() {
        });

        log.debug("auth usersResponse={}", usersResponse.getBody());

        // create a map of profiles and u
        Map<String, Boolean> activeStatusMap = usersResponse.getBody()
                .stream()
                .collect(
                        Collectors.toMap(User::getAccountId, User::isActiveStatus)
                );
        log.debug("activeStatusMap ={}", activeStatusMap);

        for (Profile profile : response.getBody().getContent()) {
            log.debug("setting profile {}/{}  ", profile, profile.getAccountId(), activeStatusMap.get(profile.getAccountId()));

            Boolean activeStatus = activeStatusMap.get(profile.getAccountId());
            log.debug("activeStatusMap.get({})={}", profile.getAccountId(), activeStatus);
            if (activeStatus != null) {
                profile.setActiveStatus(activeStatusMap.get(profile.getAccountId()));
            } else {
                log.debug("no matching activeStatus value for {}", profile.getAccountId());
            }
        }

        log.debug("response", response);

        this.administrationFindUsers.increment();
        return response.getBody();

    }

    public PagedTelemetryEventResponse<ClientTelemetryEvent> findTelemetryByPage(PagedTelemetryEventRequest request,
            String authorizationHeader) {

 

        String telemetryURL = telemetryServiceEndpointURL + "/findByAccountId";

        log.debug("telemetry url {}", telemetryURL);
        HttpHeaders headers = new HttpHeaders();

        // propagate the auth token
        headers.add("Authorization", authorizationHeader);
        HttpEntity entity = new HttpEntity(request, headers);

        // get the request page of profiles....
        ResponseEntity<PagedTelemetryEventResponse<ClientTelemetryEvent>> response = restTemplate.exchange(telemetryURL, HttpMethod.POST, entity, new ParameterizedTypeReference< PagedTelemetryEventResponse<ClientTelemetryEvent>>() {
        });
        this.administrationFindTelemetry.increment();
        log.debug("response:{}", response);
        return response.getBody();

    }

    // need to check JWT
    public boolean setUserActiveStatus(String accountId, boolean activeStatus, String authorizationHeader) {

        String url = this.authServiceEndpointURL + "/setUserActiveStatus/" + accountId;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("activeStatus", activeStatus);

        String authURL = builder.toUriString();
        log.debug("authURL={}", authURL);
        HttpHeaders authHeaders = new HttpHeaders();

        // propagate the auth token
        authHeaders.add("Authorization", authorizationHeader);

        HttpEntity<String> entity = new HttpEntity<String>("parameters", authHeaders);

        log.debug("entity={}", entity);

        ResponseEntity<Boolean> usersResponse = restTemplate.postForEntity(authURL, entity, Boolean.class);

        log.debug("userResponse={}", usersResponse);
        log.debug("userResponse.getBody()={}", usersResponse.getBody());

        if(activeStatus){
        this.administrationUserStatusEnabled.increment();
        }else{
            this.administrationUserStatusDisabled.increment();
        }
        return usersResponse.getBody();

    }
    
    @PostConstruct
    private void initializeMetrics(){
        log.debug("initializing metrics");
        administrationFindUsers = Counter.builder("administration.user.query")
                .description("The number of administration user queries.")
                .register(meterRegistry);
        
        administrationFindTelemetry=Counter.builder("administration.telemetry.query")
                .description("The number of administration telemetry queries.")
                .register(meterRegistry);
        
        administrationUserStatusEnabled = Counter.builder("administration.user.status.enabled")
                .description("The number of user statuses enabled.")
                .register(meterRegistry);
        
        administrationUserStatusDisabled = Counter.builder("administration.user.status.disbled")
                .description("The number of user statuses disabled.")
                .register(meterRegistry);
    }

}
