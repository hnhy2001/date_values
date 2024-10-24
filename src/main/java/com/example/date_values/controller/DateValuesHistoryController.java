package com.example.date_values.controller;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.dto.DateValuesHistoryDto;
import com.example.date_values.entity.DateValuesHistory;
import com.example.date_values.service.BaseService;
import com.example.date_values.service.DateValuesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("date-values-history")
public class DateValuesHistoryController extends BaseController<DateValuesHistory, DateValuesHistoryDto> {
    @Autowired
    DateValuesHistoryService service;

    public DateValuesHistoryController() {
        super(DateValuesHistoryDto.class);
    }

    @Override
    protected BaseService<DateValuesHistory> getService() {
        return service;
    }
}
