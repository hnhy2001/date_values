package com.example.date_values.model.reponse;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateValues;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRangeNumbersRes {
    private List<DateValuesDto> dateValues;
    private List<Integer> numbers;
    private List<SearchRangeNumberItemRes> dateList;
    int maxGap;
    int head;
}
