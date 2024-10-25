package com.example.date_values.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    public String token;
    public String username;
    public String phone;
    public String email;
    public String address;
    public String id;
    public String role;
}
