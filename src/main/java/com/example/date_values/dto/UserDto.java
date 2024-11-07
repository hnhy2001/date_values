package com.example.date_values.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String role;
    private String fullName;
    private String email;
    private String phone;
    private String address;
//    private int isActive;
    private int status;
}
