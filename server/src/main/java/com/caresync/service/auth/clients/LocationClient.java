package com.caresync.service.auth.clients;

import com.caresync.service.auth.dtos.request.LocationRequest;
import com.caresync.service.auth.dtos.response.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String LOCATION_SERVICE_BASE_URL = "http://location-service:8083/location/v1";

    public String test() {
        return restTemplate.getForObject(LOCATION_SERVICE_BASE_URL + "/test", String.class);
    }

    public List<LocationResponse> getAllLocations() {
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(LOCATION_SERVICE_BASE_URL + "/all", LocationResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public List<LocationResponse> getAllHospitalLocations() {
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(LOCATION_SERVICE_BASE_URL + "/hospitals", LocationResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public List<LocationResponse> getAllUserLocations() {
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(LOCATION_SERVICE_BASE_URL + "/users", LocationResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public List<LocationResponse> getAllDoctorLocations() {
        ResponseEntity<LocationResponse[]> response = restTemplate.getForEntity(LOCATION_SERVICE_BASE_URL + "/doctors", LocationResponse[].class);
        return Arrays.asList(response.getBody());
    }

    public LocationResponse getLocationById(Long id) {
        return restTemplate.getForObject(LOCATION_SERVICE_BASE_URL + "/id/" + id, LocationResponse.class);
    }

    public LocationResponse createLocation(LocationRequest locationRequest) {
        ResponseEntity<LocationResponse> response = restTemplate.postForEntity(
                LOCATION_SERVICE_BASE_URL + "/add",
                locationRequest,
                LocationResponse.class
        );
        return response.getBody();
    }

    public LocationResponse updateLocation(LocationRequest locationRequest) {
        HttpEntity<LocationRequest> entity = new HttpEntity<>(locationRequest);
        ResponseEntity<LocationResponse> response = restTemplate.exchange(
                LOCATION_SERVICE_BASE_URL + "/update",
                HttpMethod.PUT,
                entity,
                LocationResponse.class
        );
        return response.getBody();
    }

    public void deleteLocationById(Long id) {
        restTemplate.delete(LOCATION_SERVICE_BASE_URL + "/delete/id/" + id);
    }
}
