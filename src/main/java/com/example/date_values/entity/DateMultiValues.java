package com.example.date_values.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "date_multi_values")
@Builder
public class DateMultiValues extends BaseEntity {
    private Long date;
    private String value;
}