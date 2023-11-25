package com.project.georadiusservice.exception;

import com.climate.bo.MobileApiResponseBO;
import com.climate.util.FarmRiseCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class AdvisorExceptionHandler {

    @ExceptionHandler(AdvisorServiceException.class)
    public ResponseEntity<MobileApiResponseBO> handleAdvisorServiceException(AdvisorServiceException e) {
        log.error("AdvisorExceptionHandler.handleAdvisorServiceException():: exception = ", e);
        return FarmRiseCommonUtils.getResponseEntity(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MobileApiResponseBO> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("AdvisorExceptionHandler.handleConstraintViolationException():: exception = ", e);
        return FarmRiseCommonUtils.getResponseEntity(null, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}