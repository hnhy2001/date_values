package com.example.date_values.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DateMultiValuesDto {
    private Long id;
    private Long date;
    private String value;
    private int isActive;
}
