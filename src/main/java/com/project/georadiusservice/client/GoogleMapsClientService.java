package com.project.georadiusservice.client;

import com.climate.constants.LiteralConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.georadiusservice.dto.distance_matrix.DistanceMatrixApiResponseDto;
import com.project.georadiusservice.dto.google.FindPlaceApiResponseDto;
import com.project.georadiusservice.dto.google.GeoCodingApiResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.project.georadiusservice.constants.UserConstants.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
@Service
public class GoogleMapsClientService {

    @Resource(name = "googleMapsWebClient")
    private WebClient googleMapsClient;

    private ObjectMapper objectMapper;
    @Value("${google.geocoding.apikey}")
    private String googleApiKey;

    @Value("${google.maps.service.geocoder.url}")
    private String geocoderUrl;

    @Value("${google.maps.distance.matrix.url}")
    private String distanceMatrixUrl;

    @Value("${google.maps.find.place.url}")
    private String findPlaceUrl;

    public Mono<GeoCodingApiResponseDto> getGeocodingApiResponse(List<String> addressList) {
        log.info("GoogleClientService.getGeoCodingApiResponse() called with addressList={}", addressList);
        String location = String.join("+", addressList);
        String encodedLocation = UriUtils.encodePath(location, StandardCharsets.UTF_8.toString());
        MultiValueMap<String, String> geoCodingQueryParams = getGeocodingQueryParamsForAddressList(encodedLocation);
        return getGoogleMapsClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(geocoderUrl)
                        .queryParams(geoCodingQueryParams).build())
                .retrieve()
                .bodyToMono(GeoCodingApiResponseDto.class)
                .doOnSuccess(responseDto -> log.info("GoogleClientService.getGeocodingApiResponse() :: returned with response={}", responseDto))
                .doOnError(throwable -> log.info("GoogleClientService.getGeocodingApiResponse() :: exception={}", throwable.getMessage()));
    }

    public Mono<GeoCodingApiResponseDto> getGeocodingApiResponse(String placeId) {
        log.info("GoogleClientService.getGeoCodingApiResponse() called with placeId={}", placeId);
        MultiValueMap<String, String> geoCodingQueryParams = getGeocodingQueryParamsForPlaceId(placeId);
        return getGoogleMapsClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(geocoderUrl)
                        .queryParams(geoCodingQueryParams).build())
                .retrieve()
                .bodyToMono(GeoCodingApiResponseDto.class)
                .doOnSuccess(responseDto -> log.info("GoogleClientService.getGeocodingApiResponse() :: returned with response={}", responseDto))
                .doOnError(throwable -> log.info("GoogleClientService.getGeocodingApiResponse() :: exception={}", throwable.getMessage()));
    }

    public Mono<FindPlaceApiResponseDto> getFindPlaceApiResponse(List<String> addressList) {
        log.info("GoogleClientService.getFindPlaceApiResponse() called with addressList={}", addressList);
        String location = String.join("+", addressList);;
        MultiValueMap<String, String> findPlaceQueryParams = getFindPlaceQueryParams(location);
        return getGoogleMapsClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(findPlaceUrl)
                        .queryParams(findPlaceQueryParams).build())
                .retrieve()
                .bodyToMono(FindPlaceApiResponseDto.class)
                .doOnSuccess(responseDto -> log.info("GoogleClientService.getFindPlaceApiResponse() :: returned with response={}", responseDto))
                .doOnError(throwable -> log.info("GoogleClientService.getFindPlaceApiResponse() :: exception={}", throwable.getMessage()));

    }

    public Mono<GeoCodingApiResponseDto> getReverseGeocodingApiResponse(String latitude, String longitude) {
        log.info("GoogleClientService.getReverseGeocodingApiResponse() called with latitude={}, longitude={}", latitude, longitude);
        String location = String.join(",", latitude, longitude);
        String encodedLocation = UriUtils.encodePath(location, StandardCharsets.UTF_8.toString());
        MultiValueMap<String, String> geoCodingQueryParams = getReverseGeocodingQueryParams(encodedLocation);
        return getGoogleMapsClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(geocoderUrl)
                        .queryParams(geoCodingQueryParams).build())
                .retrieve()
                .bodyToMono(GeoCodingApiResponseDto.class)
                .doOnSuccess(responseDto -> log.info("GoogleClientService.getReverseGeocodingApiResponse() :: returned with response={}", responseDto))
                .doOnError(throwable -> log.info("GoogleClientService.getReverseGeocodingApiResponse() :: exception={}", throwable.getMessage()));

    }


    public Mono<DistanceMatrixApiResponseDto> getDistanceMatrixApiResponse(List<String> destinations, String origin) {
        log.info("GoogleClientService.getDistanceMatrixApiResponse() called with destinations={}, origin={}", destinations, origin);
        String destination = String.join("|", destinations);
//        String encodedDestination = destination, StandardCharsets.UTF_8.toString();
//        String encodedOrigin = UriUtils.encodePath(origin, StandardCharsets.UTF_8.toString());
        MultiValueMap<String, String> distanceMatrixQueryParams = getDistanceMatrixQueryParams(destination, origin);
        return getGoogleMapsClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(distanceMatrixUrl)
                        .queryParams(distanceMatrixQueryParams).build())
                .retrieve()
                .bodyToMono(DistanceMatrixApiResponseDto.class)
                .doOnSuccess(responseDto -> log.info("GoogleClientService.getDistanceMatrixApiResponse() :: returned with response={}", responseDto))
                .doOnError(throwable -> log.info("GoogleClientService.getDistanceMatrixApiResponse() :: exception={}", throwable.getMessage()));

    }

    private MultiValueMap<String, String> getDistanceMatrixQueryParams(String encodedDestination, String encodedOrigin) {
        Map<String, List<String>> requestMap = new LinkedMultiValueMap<>();
        requestMap.put(LiteralConstants.KEY, Collections.singletonList(googleApiKey));
        requestMap.put(DESTINATION_KEY, Collections.singletonList(encodedDestination));
        requestMap.put(ORIGIN_KEY, Collections.singletonList(encodedOrigin));
        String language = LiteralConstants.ISO_CODE_EN.toLowerCase();
        requestMap.put(LiteralConstants.LANGUAGE, Collections.singletonList(language));
        return new LinkedMultiValueMap<>(requestMap);
    }

    private MultiValueMap<String, String> getGeocodingQueryParamsForAddressList(String location) {
        Map<String, List<String>> requestMap = new LinkedMultiValueMap<>();
        requestMap.put(LiteralConstants.KEY, Collections.singletonList(googleApiKey));
        requestMap.put(ADDRESS_KEY, Collections.singletonList(location));
        String language = LiteralConstants.ISO_CODE_EN.toLowerCase();
        requestMap.put(LiteralConstants.LANGUAGE, Collections.singletonList(language));
        return new LinkedMultiValueMap<>(requestMap);
    }

    private MultiValueMap<String, String> getGeocodingQueryParamsForPlaceId(String placeId) {
        Map<String, List<String>> requestMap = new LinkedMultiValueMap<>();
        requestMap.put(LiteralConstants.KEY, Collections.singletonList(googleApiKey));
        requestMap.put(PLACE_ID, Collections.singletonList(placeId));
        String language = LiteralConstants.ISO_CODE_EN.toLowerCase();
        requestMap.put(LiteralConstants.LANGUAGE, Collections.singletonList(language));
        return new LinkedMultiValueMap<>(requestMap);
    }

    private MultiValueMap<String, String> getFindPlaceQueryParams(String location) {
        Map<String, List<String>> requestMap = new LinkedMultiValueMap<>();
        requestMap.put(LiteralConstants.KEY, Collections.singletonList(googleApiKey));
        requestMap.put(INPUT, Collections.singletonList(location));
        requestMap.put(INPUT_TYPE, Collections.singletonList(TEXT_QUERY));
        String fields = String.join(",", FORMATTED_ADDRESS, PLACE_ID, GEOMETRY);
        requestMap.put(FIELDS, Collections.singletonList(fields));
        String language = LiteralConstants.ISO_CODE_EN.toLowerCase();
        requestMap.put(LiteralConstants.LANGUAGE, Collections.singletonList(language));
        return new LinkedMultiValueMap<>(requestMap);
    }

    private MultiValueMap<String, String> getReverseGeocodingQueryParams(String location) {
        Map<String, List<String>> requestMap = new LinkedMultiValueMap<>();
        requestMap.put(LiteralConstants.KEY, Collections.singletonList(googleApiKey));
        requestMap.put(LATLNG, Collections.singletonList(location));
        String language = LiteralConstants.ISO_CODE_EN.toLowerCase();
        requestMap.put(LiteralConstants.LANGUAGE, Collections.singletonList(language));
        return new LinkedMultiValueMap<>(requestMap);
    }

}
