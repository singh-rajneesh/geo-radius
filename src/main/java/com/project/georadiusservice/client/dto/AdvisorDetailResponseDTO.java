package com.project.georadiusservice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvisorDetailResponseDTO implements Serializable {

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("customer_type")
    private String customerType;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("contact_person")
    private String contactPerson;

    @JsonProperty("account_status")
    private String accountStatus;

    @JsonProperty("geo")
    private AdvisorGeoLocation geo;

    @JsonProperty("assigned_village_apac_code")
    private List<String> assignedVillageApacCode;

    @JsonProperty("registration_timestamp")
    private String registrationTimestamp;

}