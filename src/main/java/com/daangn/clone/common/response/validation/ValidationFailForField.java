package com.daangn.clone.common.response.validation;

import lombok.*;
import org.springframework.validation.FieldError;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationFailForField {
    private String defaultMessage;
    private String objectName;
    private String field;


    public ValidationFailForField(FieldError error){
        this.defaultMessage = error.getDefaultMessage();
        this.objectName = error.getObjectName();
        this.field = error.getField();

    }
}
