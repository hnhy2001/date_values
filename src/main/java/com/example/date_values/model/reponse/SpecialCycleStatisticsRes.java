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
    private int maxStartValue;
    private int maxEndValue;
    private int lastValue;
    private int maxGap;
    private int duringValue;
    private Long duringDate;
    private long stubbornnessLevel;
    private int check;
}
