package com.example.date_values.service;

import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.GetQuantityValuesByDateAndNumberReq;

public interface DateMultiValuesService extends BaseService<DateMultiValues>{
    BaseResponse crawlAllData();
    BaseResponse getQuantityValuesByDateAndNumber(GetQuantityValuesByDateAndNumberReq req);
}
