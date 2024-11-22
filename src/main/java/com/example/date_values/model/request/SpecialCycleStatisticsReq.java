package com.example.date_values.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialCycleStatisticsReq {
    private Long startDate;
    private Long endDate;
    private List<Integer> data;
    private int gap;
}
