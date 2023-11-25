package com.project.georadiusservice.controller;

import com.climate.bo.MobileApiResponseBO;
import com.climate.util.FarmRiseCommonUtils;
import com.project.georadiusservice.bo.UserClientRequestBO;
import com.project.georadiusservice.constants.ExceptionConstants;
import com.project.georadiusservice.constants.UserConstants;
import com.project.georadiusservice.dto.AdvisorDetailDTO;
import com.project.georadiusservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * @return Return list of users for given location and radius
     */
    @GetMapping("/v1/nearby-users")
    public ResponseEntity<MobileApiResponseBO> getUserList(
            @RequestHeader(UserConstants.LATITUDE) String latitude,
            @RequestHeader(UserConstants.LONGITUDE) String longitude,
            @RequestParam(value = UserConstants.RADIUS, defaultValue = "1000") String radius,
            @RequestParam(value = UserConstants.PAGE_OFFSET, defaultValue = "0") Integer pageOffset) {
        UserClientRequestBO userClientRequestBO = UserClientRequestBO.builder().latitude(latitude).longitude(longitude)
                .radius(radius).build();
        List<AdvisorDetailDTO> advisorList = userService.getNearbyUserList(userClientRequestBO, pageOffset);
        if (CollectionUtils.isEmpty(advisorList)) {
            return FarmRiseCommonUtils.getResponseEntity(null, ExceptionConstants.NEARBY_USERS_NOT_AVAILABLE, HttpStatus.OK);
        }
        return FarmRiseCommonUtils.getResponseEntity(advisorList, null, HttpStatus.OK);
    }
}