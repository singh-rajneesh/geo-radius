package com.project.georadiusservice.mapper;

import com.project.georadiusservice.constants.UserConstants;
import com.project.georadiusservice.dto.AdvisorDetailDTO;
import com.project.georadiusservice.entity.UserDetail;
import com.project.georadiusservice.model.LocationGeoJson;
import com.project.georadiusservice.model.UserGeoLocation;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, config = AuditMapper.class,
        builder = @Builder(disableBuilder = true))
public interface UserDetailMapper {
    @InheritConfiguration
    AdvisorDetailDTO map(UserDetail source);

    @Mapping(target = "loc", expression = "java(getGeoJson(source.getLocation()))")
    @InheritConfiguration
    UserDetail map(AdvisorDetailDTO source);

    @Mapping(target = "advisorUserId", source = "customerId")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "name", source = "contactPerson")
    @Mapping(target = "status", source = "accountStatus")
    @Mapping(target = "locationHierarchy", source = "geo")
    AdvisorDetailDTO map(AMSUserDetailBO source);

    default List<AdvisorDetailDTO> map(List<UserDetail> list) {
        return list.stream().map(this::map).collect(Collectors.toList());
    }

    default LocationGeoJson getGeoJson(UserGeoLocation source) {
        List<Double> coordinates = Arrays.asList(Double.valueOf(source.getLongitude()), Double.valueOf(source.getLatitude()));
        return LocationGeoJson.builder()
                .coordinates(coordinates)
                .type(UserConstants.SUPPORTED_GEO_LOCATION_TYPE)
                .build();
    }

}