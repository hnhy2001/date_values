package com.example.date_values.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "date_values")
@Builder
@Getter
@Setter
public class DateValues extends BaseEntity {
    private Long date;
    private String value;
}
