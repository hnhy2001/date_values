package com.example.date_values.dto;

import lombok.*;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DateValuesHistoryDto {
    private Long id;

    private Integer isActive;

    private Long createDate;

    private String data;

    private Long startDate;

    private Long endDate;

    private Long maxStartDate;

    private Long maxEndDate;

    private Long lastDate;

    private int maxGap;

    private Long date;

    private long stubbornnessLevel;

    private int quantity;

    private int status;

    private String createdBy;
}
