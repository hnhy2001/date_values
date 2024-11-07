package com.example.date_values.service.impl;

import com.example.date_values.entity.DateValuesHistory;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.CreatePrimeNumbersReq;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.DateValueHistoryRepository;
import com.example.date_values.service.DateValuesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateValuesHistoryServiceImpl extends BaseServiceImpl<DateValuesHistory> implements DateValuesHistoryService {
    @Autowired
    DateValueHistoryRepository dateValueHistoryRepository;

    @Override
    protected BaseRepository<DateValuesHistory> getRepository() {
        return dateValueHistoryRepository;
    }
}
