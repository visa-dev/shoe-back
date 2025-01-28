package com.uov.exam.Response;
import com.uov.exam.model.USER_ROLE;
import lombok.Data;



@Data
public class AuthResponse {

    private String jwt ;
    private String message;
    private USER_ROLE role;
    private String username;
}
