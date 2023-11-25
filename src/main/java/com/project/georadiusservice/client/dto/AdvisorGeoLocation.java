package com.project.georadiusservice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvisorGeoLocation implements Serializable {

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("country_description")
    private String countryDesc;

    @JsonProperty("base_location_apac_code")
    private String BaseLocationApacCode;

}