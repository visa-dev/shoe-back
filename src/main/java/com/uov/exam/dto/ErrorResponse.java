package com.uov.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private String message;        // A brief message describing the error
    private String errorCode;      // A specific error code to help identify the error
    private long timestamp;        // The timestamp when the error occurred
    private String errorDescription; // Additional description about the error
}
