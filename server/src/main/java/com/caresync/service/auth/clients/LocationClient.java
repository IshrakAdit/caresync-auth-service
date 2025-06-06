package com.caresync.service.auth.clients;

import com.caresync.service.auth.dtos.request.LocationRequest;
import com.caresync.service.auth.dtos.response.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Component
@RequiredArgsConstructor
public class LocationClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String LOCATION_SERVICE_BASE_URL = "http://localhost:8083/location/v1";

    public LocationResponse createLocation(LocationRequest locationRequest) {
        ResponseEntity<LocationResponse> response = restTemplate.postForEntity(
                LOCATION_SERVICE_BASE_URL + "/add",
                locationRequest,
                LocationResponse.class
        );
        return response.getBody();
    }

    public LocationResponse getLocationById(Long id) {
        return restTemplate.getForObject(
                LOCATION_SERVICE_BASE_URL + "/id/" + id,
                LocationResponse.class
        );
    }
}
