package com.project.georadiusservice.dto.distance_matrix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrixApiResponseDto {

    @JsonProperty("destination_addresses")
    List<String> destinationAddresses;

    @JsonProperty("origin_addresses")
    List<String> originAddresses;

    private String status;

    private List<DistanceMatrixRowDto> rows;

}
