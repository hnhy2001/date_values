package com.example.date_values.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticFrequencyReq {
    private Long startDate;
    private Long endDate;
    private int head;
}
