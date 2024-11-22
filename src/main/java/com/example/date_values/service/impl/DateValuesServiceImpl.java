package com.example.date_values.service.impl;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateValues;
import com.example.date_values.entity.DateValuesHistory;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.model.reponse.SearchRangeNumberItemRes;
import com.example.date_values.model.reponse.SearchRangeNumbersRes;
import com.example.date_values.model.reponse.SpecialCycleStatisticsRes;
import com.example.date_values.model.request.CreatePrimeNumbersReq;
import com.example.date_values.model.request.SearchReq;
import com.example.date_values.model.request.SpecialCycleStatisticsReq;
import com.example.date_values.model.request.TodayNumberStatisticsReq;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.DateValuesRepository;
import com.example.date_values.service.DateValuesHistoryService;
import com.example.date_values.service.DateValuesService;
import com.example.date_values.util.DateUtil;
import com.example.date_values.util.MapperUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DateValuesServiceImpl extends BaseServiceImpl<DateValues> implements DateValuesService {

    @Autowired
    private DateValuesRepository repository;

    @Autowired
    private DateValuesHistoryService historyService;

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
                .sort("date,asc")
                .build();
        List<DateValues> dateValuesList = this.search(searchReq).getContent();
        Map<Long, Integer> dateValuesMap = new HashMap<>();
        int countGap = 1;
        int maxGap = 0;
        Long maxEndDate = 0L;
        int duringValue = 0;
        Long duringDate = 0L;
        int check = 0;
        for (DateValues element : dateValuesList) {
            if (element.getValue() != null){
                dateValuesMap.put(element.getDate(), Integer.parseInt(element.getValue()));
                if (!req.getData().contains(Integer.parseInt(element.getValue()))) {
                    countGap++;
                    if (countGap > maxGap) {
                        maxGap = countGap;
                        maxEndDate = element.getDate();
                    }
                } else {
                    countGap = 1;
                    duringDate = element.getDate();
                    duringValue = Integer.parseInt(element.getValue());
                    check = 1;
                }
            }
        }
        maxEndDate = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxEndDate : DateUtil.sum(maxEndDate, 1);
        maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
        Long maxStartDate = DateUtil.subtract(maxEndDate, maxGap);

        searchReq = SearchReq.builder()
                .filter("id>0;value=in=(" + req.getData().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")) + ")")
                .page(0)
                .size(1)
                .sort("date,desc")
                .build();

        DateValues dateValues = this.search(searchReq).getContent().get(0);
        SpecialCycleStatisticsRes res;
        if (check == 1 ){
            res = SpecialCycleStatisticsRes.builder()
                    .startDate(req.getStartDate())
                    .endDate(req.getEndDate())
                    .data(req.getData())
                    .maxStartDate(maxStartDate)
                    .maxEndDate(req.getData().contains(dateValuesMap.get(maxEndDate)) ? maxEndDate : 0)
                    .lastDate(dateValues.getDate())
                    .maxGap(maxGap)
                    .lastValue(Integer.parseInt(dateValues.getValue()))
                    .maxEndValue(req.getData().contains(dateValuesMap.get(maxEndDate)) ? dateValuesMap.get(maxEndDate) : 0)
                    .maxStartValue(dateValuesMap.get(maxStartDate))
                    .stubbornnessLevel(DateUtil.calculateDaysBetween(dateValues.getDate(), DateUtil.getCurrenDate()) - 1)
                    .duringDate(duringDate)
                    .duringValue(duringValue)
                    .check(check)
                    .build();
        }else {
            res = SpecialCycleStatisticsRes.builder()
                    .stubbornnessLevel(DateUtil.calculateDaysBetween(dateValues.getDate(), DateUtil.getCurrenDate()) - 1)
                    .lastDate(dateValues.getDate())
                    .lastValue(Integer.parseInt(dateValues.getValue()))
                    .check(check)
                    .build();
        }
        return res;
    }

    @Override
    public BaseResponse getTodayNumbersStatistics(TodayNumberStatisticsReq req) throws Exception {
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
                check = 23;
                break;
            case 50:
                check = 22;
                break;
            case 55:
                check = 21;
                break;
        }

        List<Integer> checkList = new ArrayList<>();
        int merge = 0;
        do {
            SearchReq searchReq = SearchReq.builder()
                    .filter("id>0")
                    .page(0)
                    .size(check + 1)
                    .sort("date,desc")
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
            if (result.getMaxGap() - result.getStubbornnessLevel() < 5){
                DateValuesHistory dateValuesHistory = DateValuesHistory.builder()
                        .startDate(result.getStartDate())
                        .endDate(result.getEndDate())
                        .maxStartDate(result.getMaxStartDate())
                        .maxEndDate(result.getMaxEndDate())
                        .lastDate(result.getLastDate())
                        .maxGap(result.getMaxGap())
                        .stubbornnessLevel(result.getStubbornnessLevel())
                        .data(result.getData().toString())
                        .date(DateUtil.getCurrenDate())
                        .status(0)
                        .quantity(result.getData().size())
                        .build();
                historyService.create(dateValuesHistory);
                return new BaseResponse().success(result);
            }

        }

        return new BaseResponse().fail("Vui lòng thử lại lần nữa");
    }

    @Override
    public BaseResponse crawlData(Long date) {
        String dateString = DateUtil.dateLongToString(date);
        if (repository.existsByDate(date)) {
            return new BaseResponse().fail(String.format("Kết quả ngày %s đã tồn tại", dateString));
        }else{
            try {
                // Kết nối đến trang web
                String url = String.format("https://xoso.com.vn/xsmb-%s.html", dateString); // Đổi thành URL của trang web bạn muốn crawl
                Document doc = Jsoup.connect(url).timeout(5000).userAgent("Mozilla").get();

                Element valueElement = doc.selectFirst("span#mb_prizeDB_item0");
                if (valueElement != null) {
                    String value = valueElement.text().substring(valueElement.text().length() - 2); // Lấy nội dung text bên trong thẻ div
                    DateValues dateValues = DateValues.builder()
                            .date(date)
                            .value(value)
                            .build();
                    return new BaseResponse().success(this.create(dateValues));
                } else {
                    return new BaseResponse().fail("Lấy dữ liệu thất bại");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new BaseResponse().fail("Fail!");
        }

    }

    @Override
    public BaseResponse createPrimeNumbers(SpecialCycleStatisticsReq req) throws Exception {
        SpecialCycleStatisticsRes result = find(req);
        DateValuesHistory dateValuesHistory = DateValuesHistory.builder()
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .maxStartDate(result.getMaxStartDate())
                .maxEndDate(result.getMaxEndDate())
                .lastDate(result.getLastDate())
                .maxGap(result.getMaxGap())
                .stubbornnessLevel(result.getStubbornnessLevel())
                .data(result.getData().toString())
                .date(DateUtil.getCurrenDate())
                .status(1)
                .quantity(result.getData().size())
                .build();
        historyService.create(dateValuesHistory);
        return new BaseResponse().success(dateValuesHistory);
    }

    @Override
    public BaseResponse searchRangeNumbers(SpecialCycleStatisticsReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("date,asc")
                .build();
        List<DateValues> dateValuesList = this.search(searchReq).getContent();
        if (req.getData().isEmpty()){
            SearchRangeNumbersRes result = SearchRangeNumbersRes.builder()
                    .dateValues(MapperUtil.mapEntityListIntoDtoPage(dateValuesList, DateValuesDto.class))
//                    .numbers(req.getData())
//                    .dateList(itemList)
                    .build();
            return new BaseResponse().success(result);
        }
        SpecialCycleStatisticsRes findResult = this.find(req);
        int maxGapCheck = findResult.getMaxGap();
        if (req.getGap() > maxGapCheck){
            return new BaseResponse().fail("Ngưỡng cực đại là" + maxGapCheck + "không thể nhập số lượng lớn hơn ngưỡng cực đại!");
        }
        List<SearchRangeNumberItemRes> itemList = new ArrayList<>();
        int countGap = 1;
        int maxGap = 0;
        Long maxEndDate;
        if (req.getGap() == 0) {
            for (DateValues element : dateValuesList) {
                if (element.getValue() != null){
                    if (!req.getData().contains(Integer.parseInt(element.getValue()))) {
                        countGap++;
                        if (countGap >= maxGap) {
                            maxGap = countGap;
                            maxEndDate = element.getDate();
                            maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
                            if (maxGap == maxGapCheck) {
                                Long maxEndDateItem = maxEndDate;
//                                if (findResult.getEndDate() != 0){
//                                    maxEndDateItem = DateUtil.subtract(maxEndDateItem, 1);
//                                }
                                SearchRangeNumberItemRes item = SearchRangeNumberItemRes.builder()
                                        .to(maxEndDateItem)
                                        .from(DateUtil.subtract(maxEndDateItem, maxGap-1))
                                        .build();
                                itemList.add(item);
                            }
                        }
                    } else {
                        countGap = 1;
                    }
                }
            }
        }

        else {
            for (int i = 0; i < dateValuesList.size(); i++) {
                if (dateValuesList.get(i).getValue() != null){
                    if (!req.getData().contains(Integer.parseInt(dateValuesList.get(i).getValue()))) {
                        countGap++;
                        if (countGap == req.getGap()){
                            maxGap = countGap;
                            if (i == dateValuesList.size() - 1) {
                                maxEndDate = dateValuesList.get(i).getDate();
                                Long maxEndDateItem = maxEndDate;
                                SearchRangeNumberItemRes item = SearchRangeNumberItemRes.builder()
                                        .to(maxEndDateItem)
                                        .from(DateUtil.subtract(maxEndDateItem, countGap - 2))
                                        .build();
                                itemList.add(item);
                            }else {
                                if (req.getData().contains(Integer.parseInt(dateValuesList.get(i+1).getValue()))) {
                                    maxEndDate = dateValuesList.get(i).getDate();
                                    maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
                                    Long maxEndDateItem = maxEndDate;
                                    SearchRangeNumberItemRes item = SearchRangeNumberItemRes.builder()
                                            .to(maxEndDateItem)
                                            .from(DateUtil.subtract(maxEndDateItem, countGap - 2))
                                            .build();
                                    itemList.add(item);
                                }
                            }
                        }
                    } else {
                        countGap = 1;
                    }
                }
            }
        }

        SearchRangeNumbersRes result = SearchRangeNumbersRes.builder()
                .dateValues(MapperUtil.mapEntityListIntoDtoPage(dateValuesList, DateValuesDto.class))
                .numbers(req.getData())
                .dateList(itemList)
                .maxGap(findResult.getMaxGap())
                .build();

        return new BaseResponse().success(result);
    }


    @Scheduled(cron = "0 00 19 * * ?")
    public BaseResponse ScheduledCrawl(){
        System.out.println("a");
        return this.crawlData(DateUtil.getCurrenDate());
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
