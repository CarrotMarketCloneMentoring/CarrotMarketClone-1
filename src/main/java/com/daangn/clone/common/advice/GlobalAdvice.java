package com.daangn.clone.common.advice;

import com.daangn.clone.common.response.ApiResponse;
import com.daangn.clone.common.response.ApiResponseStatus;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalAdvice {

    /** 15MB 이상의 이미지 파일을 업로드 시도했을 떄 발생하는 에외에 대한 처리*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse maxFileSizeExHandler(FileSizeLimitExceededException e){
        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e, e.getMessage());
        return ApiResponse.fail(ApiResponseStatus.MAX_FILE_SIZE_EXCEEDED);
    }

    /**  총 업로드한 이미지 파일의 총양이 , 정해진 수치를 넘었을 떄 발생하는 예외에 대한 처리*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse maxRequestSizeExHandler(SizeLimitExceededException e){
        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e, e.getMessage());
        return ApiResponse.fail(ApiResponseStatus.MAX_REQUEST_SIZE_EXCEEDED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse expiredJwtExHandler(ExpiredJwtException e){
        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e, e.getMessage());
        return ApiResponse.fail(ApiResponseStatus.INVALID_JWT_TOKEN);
    }
}
