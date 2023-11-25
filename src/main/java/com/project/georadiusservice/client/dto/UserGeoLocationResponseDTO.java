package com.project.georadiusservice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGeoLocationResponseDTO extends BaseDTO {

    private Double latitude;

    private Double longitude;

    private String country;

    private String countryCode;

    private String state;

    private String city;

    private String sublocalityLevel1;

    private String sublocalityLevel2;

    private String sublocalityLevel3;

    private String locality;

    private String administrativeAreaLevel3;

    private String administrativeAreaLevel2;

    private String apacInsightVillageCode;
}