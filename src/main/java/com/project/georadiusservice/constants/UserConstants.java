package com.project.georadiusservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserConstants {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static final String PAGE_OFFSET = "pageOffset";
    public static final String PAGE_LIMIT = "pageLimit";

    public static final String RADIUS = "radius";

    public static final String ZERO_RESULTS = "ZERO_RESULTS";
    public static final String ACTIVE = "ACTIVE";

    public static final Integer LEVEL_0 = 0;

    public static final String LEVEL_DESC_TEMPLATE = "location_hierarchy_level_%d_desc";
    public static final String LEVEL_CODE_TEMPLATE = "location_hierarchy_level_%d_code";
    public static final String LEVEL_NAME_TEMPLATE = "location_hierarchy_level_%d_name";

    public static final String SUPPORTED_GEO_LOCATION_TYPE = "Point";

    public static final String DESTINATION_KEY = "destinations";
    public static final String ORIGIN_KEY = "origins";
    public static final String ADDRESS_KEY = "address";
    public static final String LATLNG = "latlng";
    public static final String FIELDS = "fields";
    public static final String INPUT = "input";
    public static final String INPUT_TYPE = "inputtype";
    public static final String TEXT_QUERY = "textquery";
    public static final String S3_DATA_SYNC_DATA_CONTEXT_REGEX = "/([^/-]+)-";
    public static final String ADMIN_LEVEL_DATA_REGEX = "^administrative_area_level_\\d+$";
    public static final String AMS_BEARER_TOKEN = "ams_bearer_token";
    public static final String FORMATTED_ADDRESS = "formatted_address";
    public static final String GEOMETRY = "geometry";
    public static final String PLACE_ID = "place_id";
    public static final String LOCALITY = "locality";
    public static final String SUB_LOCALITY = "sublocality";

}