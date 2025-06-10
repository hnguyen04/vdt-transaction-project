package com.example.complain_service.common.exception;

public enum ErrorCode {
    // 4xx - Client Errors
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    CONFLICT(409, "Conflict occurred"),

    // 5xx - Server Errors
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    SERVICE_UNAVAILABLE(503, "Service unavailable"),

    // Custom App Codes
    VALIDATION_ERROR(422, "Validation failed"),
    DUPLICATE_RESOURCE(409, "Duplicate resource"),
    OPERATION_FAILED(400, "Operation failed");

    private final int httpStatusCode;
    private final String defaultMessage;

    ErrorCode(int httpStatusCode, String defaultMessage) {
        this.httpStatusCode = httpStatusCode;
        this.defaultMessage = defaultMessage;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
