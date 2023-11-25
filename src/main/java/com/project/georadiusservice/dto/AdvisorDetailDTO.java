package com.project.georadiusservice.dto;

import com.project.georadiusservice.aspect.Translate;
import com.project.georadiusservice.model.UserGeoLocation;
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
public class AdvisorDetailDTO extends BaseDTO {

    private String id;

    private String advisorUserId;

    private String advisorImageUrl;

    private String email;

    @Translate
    private String name;

    private String phoneNumber;

    private String status;

    @Translate
    private UserGeoLocation location;

    private AMSLocationDto locationHierarchy;

    private String calculatedDistance;

    private Double calculatedDistanceValue;

    private Long orderCount;

    private Integer preferredLanguageId;

    private String apacInsightVillageCode;

}