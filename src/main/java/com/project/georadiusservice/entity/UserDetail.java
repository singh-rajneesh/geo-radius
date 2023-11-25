package com.project.georadiusservice.entity;

import com.project.georadiusservice.model.LocationGeoJson;
import com.project.georadiusservice.model.UserGeoLocation;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_detail")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserDetail extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String advisorUserId;

    private String email;

    private String name;

    private String phoneNumber;

    private String status;

    private UserGeoLocation location;

    private String advisorImageUrl;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private LocationGeoJson loc;

    private Integer preferredLanguageId;


}