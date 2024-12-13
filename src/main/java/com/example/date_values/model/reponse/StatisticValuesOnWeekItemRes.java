package com.example.date_values.model.reponse;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateValues;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class StatisticValuesOnWeekItemRes {
    private int week;
    private List<DateValuesDto> items;
}
