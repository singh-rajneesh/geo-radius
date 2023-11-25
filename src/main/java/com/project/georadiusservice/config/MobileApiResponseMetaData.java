package com.project.georadiusservice.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@Builder
@JsonSerialize
@RequiredArgsConstructor
public class MobileApiResponseMetaData implements Serializable {

    private static final long serialVersionUID = -7796929907044665819L;

    private String responseCode;
}
