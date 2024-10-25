package com.example.date_values.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReq {
    String username;
    String password;
}
