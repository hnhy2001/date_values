package com.example.date_values.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StatisticValuesOnWeekReq {
    private Long startDate;
    private Long endDate;
}
