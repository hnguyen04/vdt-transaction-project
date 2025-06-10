package com.example.transaction_service.common.dto.request;

import lombok.*;

@Getter
@Setter
public abstract class BaseGetAllRequest {
    protected Integer skipCount = 0;
    protected Integer maxResultCount = 10;
    protected String keyword;
}