package com.example.date_values.controller;

import com.example.date_values.dto.DateMultiValuesDto;
import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.entity.DateValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.GetQuantityValuesByDateAndNumberReq;
import com.example.date_values.service.BaseService;
import com.example.date_values.service.DateMultiValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("date-multi-values")
public class DateMultiValuesController extends BaseController<DateMultiValues, DateMultiValuesDto>{
    public DateMultiValuesController(){
        super(DateMultiValuesDto.class);
    }
    @Autowired
    DateMultiValuesService dateMultiValuesService;

    @GetMapping("crawl-data")
    public BaseResponse crawlData(){
        return dateMultiValuesService.crawlAllData();
    }

    @Override
    protected BaseService<DateMultiValues> getService() {
        return dateMultiValuesService;
    }

    @PostMapping("quantity-values-by-date-and-numbers")
    public BaseResponse getQuantityValuesByDateAndNumber(@RequestBody GetQuantityValuesByDateAndNumberReq req){
        return dateMultiValuesService.getQuantityValuesByDateAndNumber(req);
    }
}