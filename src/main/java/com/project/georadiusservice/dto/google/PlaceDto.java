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
public class PlaceDto {

    @JsonProperty("formatted_address")
    private String formattedAddress;

    private GeometryDto geometry;

    @JsonProperty("place_id")
    private String placeId;

}
