package com.project.georadiusservice.service.impl;

import com.project.georadiusservice.bo.UserClientRequestBO;
import com.project.georadiusservice.client.GoogleMapsClientService;
import com.project.georadiusservice.constants.UserConstants;
import com.project.georadiusservice.dto.AdvisorDetailDTO;
import com.project.georadiusservice.dto.distance_matrix.DistanceMatrixApiResponseDto;
import com.project.georadiusservice.dto.distance_matrix.DistanceMatrixElementDto;
import com.project.georadiusservice.dto.distance_matrix.TextValueDto;
import com.project.georadiusservice.mapper.UserDetailMapper;
import com.project.georadiusservice.repository.UserDetailRepository;
import com.project.georadiusservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDetailRepository userDetailRepository;

    private final UserDetailMapper userDetailMapper;

    private final GoogleMapsClientService googleMapsClientService;

    @Value("${advisors.list.default.limit}")
    private Integer advisorListDefaultLimit;

    @Value("${advisors.list.max.distance.meters}")
    private Double advisorMaxDistanceInMeters;

    @Override
    public List<AdvisorDetailDTO> getNearbyUserList(UserClientRequestBO userClientRequestBO, Integer offset) {
        Double latitude = Double.parseDouble(userClientRequestBO.getLatitude());
        Double longitude = Double.parseDouble(userClientRequestBO.getLongitude());
        List<AdvisorDetailDTO> nearbyAdvisors = userDetailMapper.map(userDetailRepository
                        .getNearbyAdvisors(latitude, longitude, advisorListDefaultLimit, offset, advisorMaxDistanceInMeters))
                .stream().filter(advisorDetailDTO -> advisorDetailDTO.getStatus().equals(UserConstants.ACTIVE)).collect(Collectors.toList());
        nearbyAdvisors = populateDistanceToUser(nearbyAdvisors, userClientRequestBO.getLatitude(), userClientRequestBO.getLongitude());
        nearbyAdvisors = nearbyAdvisors.stream().filter(advisorDetailDTO ->
                        isCalculatedDistanceValid(advisorDetailDTO, advisorMaxDistanceInMeters))
                .collect(Collectors.toList());
        return nearbyAdvisors;
    }

    public List<AdvisorDetailDTO> populateDistanceToUser(List<AdvisorDetailDTO> advisors, String userLatitude, String userLongitude) {
        if (CollectionUtils.isEmpty(advisors)) {
            return Collections.emptyList();
        }
        List<String> destinations = advisors.stream()
                .map(advisorDetailDTO -> String.join(",", advisorDetailDTO.getLocation().getLatitude(),
                        advisorDetailDTO.getLocation().getLongitude()))
                .collect(Collectors.toList());
        String origin = String.join(",", userLatitude, userLongitude);
        if (!CollectionUtils.isEmpty(destinations)) {
            DistanceMatrixApiResponseDto response = googleMapsClientService.getDistanceMatrixApiResponse(destinations, origin).block();
            if (Objects.nonNull(response) && Objects.nonNull(response.getRows())) {
                List<DistanceMatrixElementDto> elementDtos = response.getRows().get(0).getElements();
                List<TextValueDto> distances = elementDtos.stream()
                        .map(element -> {
                            if (!element.getStatus().equals("OK")) {
                                return TextValueDto.builder().text(element.getStatus()).build();
                            }
                            return element.getDistance();
                        }).collect(Collectors.toList());
                AtomicReference<Integer> counter = new AtomicReference<>(0);
                distances.forEach(distance -> {
                    advisors.get(counter.get()).setCalculatedDistance(distance.getText());
                    advisors.get(counter.get()).setCalculatedDistanceValue(Double.valueOf(distance.getValue()));
                    counter.getAndSet(counter.get() + 1);
                });
            }
        }
        return advisors;
    }

    private Boolean isCalculatedDistanceValid(AdvisorDetailDTO advisorDetailDTO, Double advisorMaxDistanceInMeters) {
        Double calculatedDistanceValue = advisorDetailDTO.getCalculatedDistanceValue();
        return calculatedDistanceValue > advisorMaxDistanceInMeters ? Boolean.FALSE : Boolean.TRUE;
    }

}