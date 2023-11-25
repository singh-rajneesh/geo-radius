package com.project.georadiusservice.dto.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeometryDto {

    private LocationDto location;

    @JsonProperty("location_type")
    private String locationType;

    private ViewportDto viewport;


}
