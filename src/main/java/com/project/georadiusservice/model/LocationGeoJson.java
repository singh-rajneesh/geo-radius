package com.project.georadiusservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LocationGeoJson {

    private String type;

    private List<Double> coordinates;
}
