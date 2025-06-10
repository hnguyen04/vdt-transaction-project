package com.example.auth_service.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(AppException.class);

    private final ErrorCode errorCode;
    private final String messageToUse;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.messageToUse = errorCode.getDefaultMessage();
        logger.error("AppException thrown with ErrorCode: {}, Message: {}", errorCode, messageToUse);
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.messageToUse = customMessage;
        logger.error("AppException thrown with ErrorCode: {}, Message: {}", errorCode, customMessage);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessageToUse() {
        return messageToUse;
    }
}
