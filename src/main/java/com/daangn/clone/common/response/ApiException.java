package com.daangn.clone.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class ApiException extends RuntimeException{

    private ApiResponseStatus status;
    private String internalMessage;

    public ApiException(ApiResponseStatus status, String internalMessage){
        this.status = status;
        this.internalMessage = internalMessage;
    }
}
