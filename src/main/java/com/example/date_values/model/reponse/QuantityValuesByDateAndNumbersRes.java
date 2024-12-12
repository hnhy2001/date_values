package com.example.date_values.model.reponse;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
public class QuantityValuesByDateAndNumbersRes {
    List<QuantityValuesByDateAndNumbersItemRes> data;
}
