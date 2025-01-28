package com.uov.exam.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private String status;  // success or error
    private String message; // description of the result
    private T data;         // data, optional, can be null for non-data responses

}
