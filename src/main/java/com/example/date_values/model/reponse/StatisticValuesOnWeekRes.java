package com.example.date_values.model.reponse;

import com.example.date_values.entity.DateValues;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StatisticValuesOnWeekRes {
    private List<StatisticValuesOnWeekItemRes> items;
    private List<String> uniqueValues;
}
