package com.example.date_values.service.impl;

import com.example.date_values.entity.DateValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.reponse.SpecialCycleStatisticsRes;
import com.example.date_values.model.request.SearchReq;
import com.example.date_values.model.request.SpecialCycleStatisticsReq;
import com.example.date_values.model.request.TodayNumberStatisticsReq;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.DateValuesRepository;
import com.example.date_values.service.DateValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.DateUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class DateValuesServiceImpl extends BaseServiceImpl<DateValues> implements DateValuesService {

    @Autowired
    private DateValuesRepository repository;

    @Override
    protected BaseRepository<DateValues> getRepository() {
        return repository;
    }

    @Override
    public BaseResponse getSpecialCycleStatistics(SpecialCycleStatisticsReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        if (req.getData().isEmpty()) {
            return new BaseResponse().fail("Danh sách thống kê không được trống!");
        }
        return new BaseResponse().success(find(req));
    }

    public SpecialCycleStatisticsRes find(SpecialCycleStatisticsReq req) {
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("id,asc")
                .build();
        List<DateValues> dateValuesList = this.search(searchReq).getContent();
        int countGap = 1;
        int maxGap = 0;
        Long lastDate = 0L;
        Long maxEndDate = 0L;

        for (DateValues element : dateValuesList) {
            if (!req.getData().contains(Integer.parseInt(element.getValue()))) {
                countGap++;
                if (countGap > maxGap) {
                    maxGap = countGap;
                    maxEndDate = element.getDate();
                }
            } else {
                countGap = 1;
                lastDate = element.getDate();
            }
        }
        maxEndDate = DateUtil.sum(maxEndDate, 1);
        Long maxStartDate = DateUtil.subtract(maxEndDate, maxGap);

        searchReq = SearchReq.builder()
                .filter("id>0;value=in=(" + req.getData().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")) + ")")
                .page(0)
                .size(1)
                .sort("id,desc")
                .build();

        DateValues dateValues = this.search(searchReq).getContent().get(0);

        SpecialCycleStatisticsRes res = SpecialCycleStatisticsRes.builder()
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .data(req.getData())
                .maxStartDate(maxStartDate)
                .maxEndDate(maxEndDate)
                .lastDate(lastDate)
                .maxGap(maxGap)
                .stubbornnessLevel(DateUtil.calculateDaysBetween(dateValues.getDate(), DateUtil.getCurrenDate()) - 1)
                .build();
        return res;
    }

    @Override
    public BaseResponse getTodayNumbersStatistics(TodayNumberStatisticsReq req) {
//        if (req.getOperatorType() != 2 && req.getOperatorType() != 1){
//            return new BaseResponse().fail("OperatorType chỉ là 1 hoặc 2");
//        }
        if (req.getQuantity() != 10 && req.getQuantity() != 15 && req.getQuantity() != 20 && req.getQuantity() != 25 && req.getQuantity() != 30
                && req.getQuantity() != 35 && req.getQuantity() != 40 && req.getQuantity() != 45 && req.getQuantity() != 50 && req.getQuantity() != 55) {
            return new BaseResponse().fail("quantity chỉ là 10,15,20,25,30,35,40,45,50,55");
        }

        int check = 0;
        switch (req.getQuantity()) {
            case 10:
                check = 65;
                break;
            case 15:
                // code block to be executed if expression equals value2
                check = 50;
                break; // optional
            case 20:
                check = 45;
                break;
            case 25:
                check = 40;
                break;
            case 30:
                check = 35;
                break;
            case 35:
                check = 30;
                break;
            case 40:
                check = 25;
                break;
            case 45:
                check = 20;
                break;
            case 50:
                check = 15;
                break;
            case 55:
                check = 10;
                break;
        }

        List<Integer> checkList = new ArrayList<>();
        int merge = 0;
        do {
            SearchReq searchReq = SearchReq.builder()
                    .filter("id>0")
                    .page(0)
                    .size(check + 1)
                    .sort("id,desc")
                    .build();
            List<DateValues> tempDateValues = this.search(searchReq).getContent();
            List<DateValues> dateValues = new ArrayList<>(tempDateValues);
            merge = Integer.parseInt(dateValues.remove(check).getValue());
            checkList = dateValues.stream().map(e -> Integer.parseInt(e.getValue())).collect(Collectors.toList());
            check++;
        }while (checkList.contains(merge));

        for (int i = 0; i < 1000000; i++) {
            List<Integer> uniqueRandomNumbers = generateUniqueRandomNumbers(req.getQuantity() - 1, checkList);
            uniqueRandomNumbers.add(merge);
            SpecialCycleStatisticsRes result = find(SpecialCycleStatisticsReq.builder().startDate(20100101L).endDate(DateUtil.getCurrenDate()).data(uniqueRandomNumbers).build());
            System.out.println(result.getMaxGap() + "-" + result.getStubbornnessLevel());
            System.out.println(uniqueRandomNumbers);
            if (result.getMaxGap() - result.getStubbornnessLevel() < 5){
                return new BaseResponse().success(result);
            }

        }

        return new BaseResponse().fail("Vui lòng thử lại lần nữa");
    }

    public static List<Integer> generateUniqueRandomNumbers(int n, List<Integer> existingList) {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();

        // Add all numbers from the existing list to a set for easy lookup
        Set<Integer> existingSet = new HashSet<>(existingList);

        // Generate unique random numbers until we reach the desired count
        while (uniqueNumbers.size() < n) {
            int randomNumber = random.nextInt(100); // Random number between 0 and 99

            // Check if the random number is not in the existing list
            if (!existingSet.contains(randomNumber)) {
                uniqueNumbers.add(randomNumber);
            }
        }

        return new ArrayList<>(uniqueNumbers);
    }


}
