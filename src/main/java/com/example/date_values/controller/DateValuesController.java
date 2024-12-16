package com.example.date_values.controller;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.request.*;
import com.example.date_values.service.BaseService;
import com.example.date_values.service.DateValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("")
public class DateValuesController extends BaseController<DateValues, DateValuesDto> {

    public DateValuesController() {
        super(DateValuesDto.class);
    }

    @Autowired
    private DateValuesService dateValuesService;

    @Override
    protected BaseService<DateValues> getService() {
        return dateValuesService;
    }

    @PostMapping("statistic")
    public BaseResponse statistic(@RequestBody SpecialCycleStatisticsReq req){
        return dateValuesService.getSpecialCycleStatistics(req);
    }

    @PostMapping("statistic-today-number")
    public BaseResponse getTodayNumbersStatistics(@RequestBody TodayNumberStatisticsReq req) throws Exception {
        return dateValuesService.getTodayNumbersStatistics(req);
    }

    @PostMapping("check")
    public List<Integer> check(@RequestBody SpecialCycleStatisticsReq req){
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("id,asc")
                .build();
        List<DateValues> checkList = this.getService().search(searchReq).getContent();
        for (int i = 0; i < checkList.size(); i++) {
            if (req.getData().contains(Integer.parseInt(checkList.get(i).getValue()))){
                System.out.println(checkList.get(i));
            }
        }
        return req.getData();
    }

    @GetMapping("crawl-data")
    public BaseResponse crawlData(@RequestParam Long date){
        return dateValuesService.crawlData(date);
    }

    @PostMapping("prime-numbers")
    public BaseResponse createprimeNumbers(@RequestBody SpecialCycleStatisticsReq req) throws Exception {
        return dateValuesService.createPrimeNumbers(req);
    }

    @PostMapping("search-rage-numbers")
    public BaseResponse searchRangeNumbers(@RequestBody SpecialCycleStatisticsReq req) throws Exception {
        return dateValuesService.searchRangeNumbers(req);
    }

    @GetMapping("crawl-all-data")
    public BaseResponse crawlData(){
        return dateValuesService.crawlAllData();
    }

    @PostMapping("statistic-values-on-week")
    public BaseResponse statisticValuesOnWeek(@RequestBody StatisticValuesOnWeekReq req) throws Exception {
        return dateValuesService.statisticValuesOnWeek(req);
    }

    @PostMapping("statistic-frequency")
    public BaseResponse statisticFrequency(@RequestBody StatisticFrequencyReq req) throws Exception {
        return dateValuesService.statisticFrequency(req);
    }
}
