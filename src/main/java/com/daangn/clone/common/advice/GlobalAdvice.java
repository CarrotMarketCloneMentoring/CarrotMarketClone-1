package com.daangn.clone.common.advice;

import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponse;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.common.response.validation.ValidationFail;
import com.daangn.clone.common.response.validation.ValidationFailForField;
import com.daangn.clone.common.response.validation.ValidationFailForObject;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.daangn.clone.common.response.ApiResponseStatus.INVALID_ENUM;

@Slf4j
@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse registerItemExHandler(ApiException e) {
        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e.getStatus(), e.getInternalMessage());
        return ApiResponse.fail(e.getStatus());
    }

    /** 필수 헤더가 없을 때 발생하는 에외 처리*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse missRequestHeaderExHandler(MissingRequestHeaderException e){
        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e, e.getMessage());
        return ApiResponse.fail(ApiResponseStatus.NO_USERNAME);
    }

    /** 필수 쿼리파라미터가 없을 떄 발생하는 예외 처리 */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse missRequestParamExHandler(MissingServletRequestParameterException e){

        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e, e.getMessage());

        if(e.getParameterName().equals("itemId")){
            return ApiResponse.fail(ApiResponseStatus.NO_ITEM_ID);
        } else if(e.getParameterName().equals("path")){
            return ApiResponse.fail(ApiResponseStatus.NO_PATH);
        } else{
            return ApiResponse.success(null);
        }
    }

    /** Bean Validation에 따른 검증에서 오류가 존재하여 BindingResult에 FieldError OR ObjectError가 있음에도 Controller에서 이에 대한
     * Handling을 하지 않는 경우 -> BindException이 터짐 -> 그러면 이 ExceptionHandler까지 예외가 올라오고, 여기서 일괄적으로 처리*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse bindExHandler(BindException e, BindingResult bindingResult){
        log.error("EXCEPTION = {} , INTERNAL_MESSAGE = {}", e, e.getMessage());
        ValidationFail validationFail = makeValidationError(bindingResult);
        return ApiResponse.failBeanValidation(validationFail);
    }

    private ValidationFail makeValidationError(BindingResult bindingResult){
        return  ValidationFail.builder()
                .fieldList(bindingResult.getFieldErrors().stream()
                        .map(f -> new ValidationFailForField(f))
                        .collect(Collectors.toList()))
                .objectList(bindingResult.getGlobalErrors().stream()
                        .map(o -> new ValidationFailForObject(o))
                        .collect(Collectors.toList()))
                .build();

    }

    /** 정의되지 않은 enum값이 넘어왔을 때 터지는 Exception을 처리하는 ExceptionHandler */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse inCorrectEnum(HttpMessageNotReadableException e){
        log.error("EXCEPTION = {} , EXCEPTION_MESSAGE = {}, INTERNAL_MESSAGE = {}", INVALID_ENUM, e.getMessage(),"정의하지 않은, 잘못된 enum 값이 요청으로 들어왔습니다.");
        return ApiResponse.fail(INVALID_ENUM);
    }



    /** ------------------------------------------------------------------------------------------------------ */

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
