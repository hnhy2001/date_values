package com.example.date_values.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "date_values_history")
@Builder
public class DateValuesHistory extends BaseEntity{
    @Column(name = "data")
    private String data;

    @Column(name = "date")
    private Long date;

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @Column(name = "max_start_date")
    private Long maxStartDate;

    @Column(name = "max_end_date")
    private Long maxEndDate;

    @Column(name = "lastDate")
    private Long lastDate;

    @Column(name = "max_gap")
    private int maxGap;

    @Column(name = "stubborness_level")
    private long stubbornnessLevel;

}
