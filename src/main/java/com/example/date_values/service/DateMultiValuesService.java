package com.example.date_values.service;

import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.model.reponse.BaseResponse;

public interface DateMultiValuesService extends BaseService<DateMultiValues>{
    BaseResponse crawlAllData();
}
