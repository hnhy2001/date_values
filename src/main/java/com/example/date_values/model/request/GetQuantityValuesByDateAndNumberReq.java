package com.example.date_values.model.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
public class GetQuantityValuesByDateAndNumberReq {
    private Long startDate;
    private Long endDate;
    private List<String> numbers;
    private int head;
}
