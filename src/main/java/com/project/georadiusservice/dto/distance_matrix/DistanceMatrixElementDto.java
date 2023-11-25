package com.project.georadiusservice.dto.distance_matrix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrixElementDto {

    private TextValueDto distance;
    private TextValueDto duration;
    private String status;
}
