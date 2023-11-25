package com.project.georadiusservice.dto.google;

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
public class FindPlaceApiResponseDto {

    private List<PlaceDto> candidates;

    private String status;

    @JsonProperty("error_message")
    private String errorMessage;

}
