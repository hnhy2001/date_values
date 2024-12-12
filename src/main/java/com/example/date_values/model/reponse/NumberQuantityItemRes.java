package com.example.date_values.model.reponse;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
public class NumberQuantityItemRes {
    private String number;
    private int quantity;
}
