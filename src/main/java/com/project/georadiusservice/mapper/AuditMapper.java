package com.project.georadiusservice.mapper;

import com.project.georadiusservice.entity.BaseEntity;
import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@MapperConfig(builder = @Builder(disableBuilder = true))
public interface AuditMapper {


    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "externalId", source = "externalId")
    BaseDTO map(BaseEntity source);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "externalId", source = "externalId")
    BaseEntity map(BaseDTO source);

}

