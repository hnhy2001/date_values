package com.example.date_values.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordReq {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
