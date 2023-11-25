package com.project.georadiusservice.exception;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.io.Serializable;

public class AdvisorServiceException extends AmqpRejectAndDontRequeueException implements Serializable {

    private static final long serialVersionUID = 12112351251L;

    private String message;

    public AdvisorServiceException(String message) {
        super(message);
    }
}
