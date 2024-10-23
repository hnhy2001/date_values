package com.example.date_values.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialCycleStatisticsRes {
    private List<Integer> data;
    private Long startDate;
    private Long endDate;
    private Long maxStartDate;
    private Long maxEndDate;
    private Long lastDate;
    private int maxGap;
    private long stubbornnessLevel;
}
