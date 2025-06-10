package com.example.transaction_service.common.exception;

import com.example.transaction_service.common.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        logger.error("Handling AppException: errorCode={}, message={}", ex.getErrorCode(), ex.getMessageToUse());

        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .code(ex.getErrorCode().getHttpStatusCode())
                .message(ex.getMessageToUse())
                .error(ex.getErrorCode().name())
                .build();

        return ResponseEntity.status(ex.getErrorCode().getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        logger.error("Unhandled exception caught: ", ex);

        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .code(500)
                .message("Internal server error")
                .error("INTERNAL_SERVER_ERROR")
                .build();

        return ResponseEntity.status(500).body(response);
    }
}
