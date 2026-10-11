package com.aymen.saas.exceptions.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String message;
    private String code;
    private String path;
    private List<ValidationErrors> validationError;

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ValidationErrors {
        private String field;
        private String message;
        private String code;
    }



}
