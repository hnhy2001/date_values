package com.example.date_values.service;

import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.GetQuantityValuesByDateAndNumberReq;
import com.example.date_values.model.request.StatisticFrequencyReq;
import com.example.date_values.model.request.StatisticMultiValuesReq;
import com.example.date_values.model.request.TodayNumberStatisticsReq;

public interface DateMultiValuesService extends BaseService<DateMultiValues>{
    BaseResponse crawlAllData();
    BaseResponse getQuantityValuesByDateAndNumber(GetQuantityValuesByDateAndNumberReq req);
    BaseResponse statistic(StatisticMultiValuesReq req);
    BaseResponse getTodayNumbersStatistics(TodayNumberStatisticsReq req);
}
