package com.project.georadiusservice.controller;

import com.climate.bo.MobileApiResponseBO;
import com.climate.util.FarmRiseCommonUtils;
import com.project.georadiusservice.bo.UserClientRequestBO;
import com.project.georadiusservice.constants.ExceptionConstants;
import com.project.georadiusservice.dto.AdvisorDetailDTO;
import com.project.georadiusservice.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserControllerTest {

    private static final String latitude = "6789.32";
    private static final String longitude = "428309.32";
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    @Test
    public void getAdvisorList() {
        ResponseEntity<MobileApiResponseBO> res = FarmRiseCommonUtils.getResponseEntity(getAdvisorDetailDto(), ExceptionConstants.NEARBY_USERS_NOT_AVAILABLE, HttpStatus.OK);
        when(userService.getNearbyUserList(getAdvisorClientRequestBO(), 0)).thenReturn(getAdvisorDetailDto());
        ResponseEntity<MobileApiResponseBO> actualRes = userController.getUserList(latitude, longitude, "1000", 0);
        assertEquals(res, actualRes);
    }

    private List<AdvisorDetailDTO> getAdvisorDetailDto() {
        return Collections.emptyList();
    }

    private UserClientRequestBO getAdvisorClientRequestBO() {
        return UserClientRequestBO.builder().latitude(latitude).longitude(longitude).build();
    }

}