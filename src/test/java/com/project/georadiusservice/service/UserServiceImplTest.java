package com.project.georadiusservice.service;

import com.project.georadiusservice.bo.UserClientRequestBO;
import com.project.georadiusservice.client.GoogleMapsClientService;
import com.project.georadiusservice.dto.AdvisorDetailDTO;
import com.project.georadiusservice.dto.distance_matrix.DistanceMatrixApiResponseDto;
import com.project.georadiusservice.dto.distance_matrix.DistanceMatrixElementDto;
import com.project.georadiusservice.dto.distance_matrix.DistanceMatrixRowDto;
import com.project.georadiusservice.dto.distance_matrix.TextValueDto;
import com.project.georadiusservice.entity.UserDetail;
import com.project.georadiusservice.mapper.UserDetailMapper;
import com.project.georadiusservice.model.UserGeoLocation;
import com.project.georadiusservice.repository.UserDetailRepository;
import com.project.georadiusservice.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    private static final String advisorId = "321312";
    private static final String userLatitude = "41.8781";
    private static final String userLongitude = "-87.6298";
    private static final String advisorLatitude = "41.8781";
    private static final String advisorLongitude = "-87.6298";
    private static final String advisorName = "Ram Kumar";
    @Mock
    private UserDetailRepository userDetailRepository;
    @Mock
    private UserDetailMapper userDetailMapper;
    @Mock
    private GoogleMapsClientService googleMapsClientService;
    @InjectMocks
    private UserServiceImpl advisorService;

    @Test
    public void testGetNearbyAdvisorList() {
        UserClientRequestBO requestBO = new UserClientRequestBO();
        requestBO.setLatitude(userLatitude);
        requestBO.setLongitude(userLongitude);
        Integer offset = 0;
        List<UserDetail> userDetailList = new ArrayList<>();
        List<AdvisorDetailDTO> advisorDetailDTOList = new ArrayList<>();
        AdvisorDetailDTO advisorDetailDTO = AdvisorDetailDTO.builder().id(advisorId).name(advisorName).build();
        advisorDetailDTOList.add(advisorDetailDTO);
        UserDetail userDetail = UserDetail.builder().id(advisorId).name(advisorName).build();
        userDetailList.add(userDetail);
        when(userDetailRepository.getNearbyAdvisors(Double.valueOf(userLatitude), Double.valueOf(userLongitude), 5, 0, 100000.0)).thenReturn(userDetailList);
        when(userDetailMapper.map(userDetailList)).thenReturn(advisorDetailDTOList);
        List<AdvisorDetailDTO> nearbyAdvisors = advisorService.getNearbyUserList(requestBO, offset);
        verify(userDetailRepository, times(1)).getNearbyAdvisors(12.9716, 77.5946, 10, 0, 100000.0);
        verify(userDetailMapper, times(1)).map(userDetailList);
        assertNotNull(nearbyAdvisors);
        assertEquals(1, nearbyAdvisors.size());
        assertEquals(userDetail.getName(), nearbyAdvisors.get(0).getName());
    }

    @Test(expected = LMSNoContentException.class)
    public void testGetNearbyAdvisorListNoContent() {
        UserClientRequestBO requestBO = new UserClientRequestBO();
        requestBO.setLatitude(userLatitude);
        requestBO.setLongitude(userLongitude);
        Integer offset = 0;
        List<UserDetail> userDetailList = new ArrayList<>();
        when(userDetailRepository.getNearbyAdvisors(Double.valueOf(userLatitude), Double.valueOf(userLongitude), 5, 0, 100000.0)).thenReturn(userDetailList);
        advisorService.getNearbyUserList(requestBO, offset);
    }

    @Test
    public void testPopulateDistanceToUser() {
        List<AdvisorDetailDTO> advisors = new ArrayList<>();
        AdvisorDetailDTO advisor1 = new AdvisorDetailDTO();
        UserGeoLocation location1 = new UserGeoLocation();
        UserGeoLocation location2 = new UserGeoLocation();
        location1.setLatitude(advisorLatitude);
        location1.setLongitude(advisorLongitude);
        advisor1.setLocation(location1);
        advisors.add(advisor1);
        AdvisorDetailDTO advisor2 = new AdvisorDetailDTO();
        location2.setLatitude("40.7128");
        location2.setLongitude("-74.0060");
        advisor2.setLocation(location2);
        advisors.add(advisor2);
        DistanceMatrixApiResponseDto responseDto = new DistanceMatrixApiResponseDto();
        DistanceMatrixRowDto rowDto = new DistanceMatrixRowDto();
        DistanceMatrixElementDto elementDto1 = new DistanceMatrixElementDto();
        TextValueDto distanceDto1 = new TextValueDto();
        distanceDto1.setText("50 mi");
        elementDto1.setDistance(distanceDto1);
        DistanceMatrixElementDto elementDto2 = new DistanceMatrixElementDto();
        TextValueDto distanceDto2 = new TextValueDto();
        distanceDto2.setText("20 mi");
        elementDto2.setDistance(distanceDto2);
        List<DistanceMatrixElementDto> elements = new ArrayList<>();
        elements.add(elementDto1);
        elements.add(elementDto2);
        rowDto.setElements(elements);
        List<DistanceMatrixRowDto> rows = new ArrayList<>();
        rows.add(rowDto);
        responseDto.setRows(rows);
        when(googleMapsClientService.getDistanceMatrixApiResponse(anyList(), anyString())).thenReturn(Mono.just(responseDto));
        List<AdvisorDetailDTO> result = advisorService.populateDistanceToUser(advisors, userLatitude, userLongitude);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getCalculatedDistance(), "50 mi");
        assertEquals(result.get(1).getCalculatedDistance(), "20 mi");
    }

}