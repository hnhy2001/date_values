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
public class StatisticMultiValuesReq {
    private Long startDate;
    private Long endDate;
    private List<Integer> values;
    private int concurOccur;
}
