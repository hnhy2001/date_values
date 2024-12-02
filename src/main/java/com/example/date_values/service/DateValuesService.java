package com.example.date_values.service;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.reponse.SpecialCycleStatisticsRes;
import com.example.date_values.model.request.CreatePrimeNumbersReq;
import com.example.date_values.model.request.SpecialCycleStatisticsReq;
import com.example.date_values.model.request.TodayNumberStatisticsReq;

public interface DateValuesService extends BaseService<DateValues> {
    BaseResponse getSpecialCycleStatistics(SpecialCycleStatisticsReq req);
    BaseResponse getTodayNumbersStatistics(TodayNumberStatisticsReq req) throws Exception;
    BaseResponse crawlData(Long date);
    BaseResponse createPrimeNumbers(SpecialCycleStatisticsReq req) throws Exception;
    BaseResponse searchRangeNumbers(SpecialCycleStatisticsReq req);
    BaseResponse crawlAllData();
}
