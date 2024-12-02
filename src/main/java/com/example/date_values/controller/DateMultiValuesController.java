package com.example.date_values.controller;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.entity.DateValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.service.DateMultiValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("date-multi-values")
public class DateMultiValuesController {
    @Autowired
    DateMultiValuesService dateMultiValuesService;
    @GetMapping("crawl-data")
    public BaseResponse crawlData(){
        return dateMultiValuesService.crawlAllData();
    }
}