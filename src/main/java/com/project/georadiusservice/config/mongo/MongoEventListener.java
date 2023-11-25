package com.project.georadiusservice.config.mongo;

import com.project.georadiusservice.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.Objects;
import java.util.UUID;

public class MongoEventListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        if (source instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) source;
            if (Objects.isNull(entity.getExternalId())) {
                entity.setExternalId(UUID.randomUUID().toString());
            }
        }
    }

}
