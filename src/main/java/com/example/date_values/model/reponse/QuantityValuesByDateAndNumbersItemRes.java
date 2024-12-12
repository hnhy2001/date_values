package com.example.date_values.model.reponse;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
public class QuantityValuesByDateAndNumbersItemRes {
    private Long date;
    private String values;
    List<NumberQuantityItemRes> numberQuantityItemRes;
}
