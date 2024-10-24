package com.example.date_values.repository;

import com.example.date_values.entity.DateValues;

public interface DateValuesRepository extends BaseRepository<DateValues> {
    boolean existsByDate(Long date);
}
