package com.project.georadiusservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGeoLocation {

    private String latitude;

    private String longitude;

    private String address;

    private String countryCode;

    private String countryDesc;

    private List<LocationHierarchyDto> locationHierarchy;

    private LocationDetail locationDetail;

}