package com.project.georadiusservice.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserClientRequestBO {

    private String latitude;

    private String longitude;

    private String radius;

}