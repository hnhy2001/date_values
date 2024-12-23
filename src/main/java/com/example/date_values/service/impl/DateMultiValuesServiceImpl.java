package com.example.date_values.service.impl;

import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.entity.DateValues;
import com.example.date_values.entity.DateValuesHistory;
import com.example.date_values.model.reponse.*;
import com.example.date_values.model.request.*;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.DateMultiValuesRepository;
import com.example.date_values.service.DateMultiValuesService;
import com.example.date_values.util.DateUtil;
import com.example.date_values.util.ListUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DateMultiValuesServiceImpl extends BaseServiceImpl<DateMultiValues> implements DateMultiValuesService {

    @Autowired
    DateMultiValuesRepository dateMultiValuesRepository;

    @Override
    protected BaseRepository<DateMultiValues> getRepository() {
        return dateMultiValuesRepository;
    }

    @Scheduled(cron = "0 34 18 * * ?")
    public BaseResponse crawlAllDataByDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate currentDate = LocalDate.now().minusDays(1);
        List<String> dates = new ArrayList<>();
        dates.add(currentDate.format(formatter));
        List<DateMultiValues> result = new ArrayList<>();
        for (String date : dates) {
            DateMultiValues item = new DateMultiValues();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Chuyển chuỗi ngày thành LocalDate
            LocalDate dateLocal = LocalDate.parse(date, inputFormatter);

            // Định dạng lại ngày thành YYYYMMDD
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedDate = dateLocal.format(outputFormatter);

            // Chuyển chuỗi YYYYMMDD thành Long
            Long longDate = Long.parseLong(formattedDate);
            item.setDate(longDate);

            try {
                // Kết nối đến trang web
                String url = String.format("https://xoso.com.vn/xsmb-%s.html", date); // Đổi thành URL của trang web bạn muốn crawl
                Document doc = Jsoup.connect(url).timeout(50000).userAgent("Mozilla").get();
                String values = null;
                if (doc.selectFirst("span#mb_prizeDB_item0") != null) {
                    values = doc.selectFirst("span#mb_prizeDB_item0").text() + ","
                            + doc.selectFirst("span#mb_prize1_item0").text() + ","
                            + doc.selectFirst("span#mb_prize2_item0").text() + ","
                            + doc.selectFirst("span#mb_prize2_item1").text() + ","
                            + doc.selectFirst("span#mb_prize3_item0").text() + ","
                            + doc.selectFirst("span#mb_prize3_item1").text() + ","
                            + doc.selectFirst("span#mb_prize3_item2").text() + ","
                            + doc.selectFirst("span#mb_prize3_item3").text() + ","
                            + doc.selectFirst("span#mb_prize3_item4").text() + ","
                            + doc.selectFirst("span#mb_prize3_item5").text() + ","
                            + doc.selectFirst("span#mb_prize4_item0").text() + ","
                            + doc.selectFirst("span#mb_prize4_item1").text() + ","
                            + doc.selectFirst("span#mb_prize4_item2").text() + ","
                            + doc.selectFirst("span#mb_prize4_item3").text() + ","
                            + doc.selectFirst("span#mb_prize5_item0").text() + ","
                            + doc.selectFirst("span#mb_prize5_item1").text() + ","
                            + doc.selectFirst("span#mb_prize5_item2").text() + ","
                            + doc.selectFirst("span#mb_prize5_item3").text() + ","
                            + doc.selectFirst("span#mb_prize5_item4").text() + ","
                            + doc.selectFirst("span#mb_prize5_item5").text() + ","
                            + doc.selectFirst("span#mb_prize6_item0").text() + ","
                            + doc.selectFirst("span#mb_prize6_item1").text() + ","
                            + doc.selectFirst("span#mb_prize6_item2").text() + ","
                            + doc.selectFirst("span#mb_prize7_item0").text() + ","
                            + doc.selectFirst("span#mb_prize7_item1").text() + ","
                            + doc.selectFirst("span#mb_prize7_item2").text() + ","
                            + doc.selectFirst("span#mb_prize7_item3").text();
                }
                item.setValue(values);
                item.setIsActive(1);
                result.add(item);
                System.out.println("ok");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.getRepository().saveAll(result);
        return new BaseResponse().success("check");
    }

    @Override
    public BaseResponse crawlAllData() {
        List<String> dates = getDataDate();
        List<DateMultiValues> result = new ArrayList<>();
        for (String date : dates) {
            DateMultiValues item = new DateMultiValues();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Chuyển chuỗi ngày thành LocalDate
            LocalDate dateLocal = LocalDate.parse(date, inputFormatter);

            // Định dạng lại ngày thành YYYYMMDD
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedDate = dateLocal.format(outputFormatter);

            // Chuyển chuỗi YYYYMMDD thành Long
            Long longDate = Long.parseLong(formattedDate);
            item.setDate(longDate);

            try {
                // Kết nối đến trang web
                String url = String.format("https://xoso.com.vn/xsmb-%s.html", date); // Đổi thành URL của trang web bạn muốn crawl
                Document doc = Jsoup.connect(url).timeout(50000).userAgent("Mozilla").get();
                String values = null;
                if (doc.selectFirst("span#mb_prizeDB_item0") != null) {
                    values = doc.selectFirst("span#mb_prizeDB_item0").text() + ","
                            + doc.selectFirst("span#mb_prize1_item0").text() + ","
                            + doc.selectFirst("span#mb_prize2_item0").text() + ","
                            + doc.selectFirst("span#mb_prize2_item1").text() + ","
                            + doc.selectFirst("span#mb_prize3_item0").text() + ","
                            + doc.selectFirst("span#mb_prize3_item1").text() + ","
                            + doc.selectFirst("span#mb_prize3_item2").text() + ","
                            + doc.selectFirst("span#mb_prize3_item3").text() + ","
                            + doc.selectFirst("span#mb_prize3_item4").text() + ","
                            + doc.selectFirst("span#mb_prize3_item5").text() + ","
                            + doc.selectFirst("span#mb_prize4_item0").text() + ","
                            + doc.selectFirst("span#mb_prize4_item1").text() + ","
                            + doc.selectFirst("span#mb_prize4_item2").text() + ","
                            + doc.selectFirst("span#mb_prize4_item3").text() + ","
                            + doc.selectFirst("span#mb_prize5_item0").text() + ","
                            + doc.selectFirst("span#mb_prize5_item1").text() + ","
                            + doc.selectFirst("span#mb_prize5_item2").text() + ","
                            + doc.selectFirst("span#mb_prize5_item3").text() + ","
                            + doc.selectFirst("span#mb_prize5_item4").text() + ","
                            + doc.selectFirst("span#mb_prize5_item5").text() + ","
                            + doc.selectFirst("span#mb_prize6_item0").text() + ","
                            + doc.selectFirst("span#mb_prize6_item1").text() + ","
                            + doc.selectFirst("span#mb_prize6_item2").text() + ","
                            + doc.selectFirst("span#mb_prize7_item0").text() + ","
                            + doc.selectFirst("span#mb_prize7_item1").text() + ","
                            + doc.selectFirst("span#mb_prize7_item2").text() + ","
                            + doc.selectFirst("span#mb_prize7_item3").text();
                }
                item.setValue(values);
                item.setIsActive(1);
                result.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.getRepository().saveAll(result);
        return new BaseResponse().success("check");
    }

    @Override
    public BaseResponse getQuantityValuesByDateAndNumber(GetQuantityValuesByDateAndNumberReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("date,asc")
                .build();
        List<DateMultiValues> data = this.search(searchReq).getContent();
        QuantityValuesByDateAndNumbersRes result = new QuantityValuesByDateAndNumbersRes();
        List<QuantityValuesByDateAndNumbersItemRes> resultIem = new ArrayList<>();
        for (DateMultiValues item : data) {
            List<NumberQuantityItemRes> numberQuantityItemResList = new ArrayList<>();
            for (String number : req.getNumbers()) {
                NumberQuantityItemRes numberQuantityItemRes = NumberQuantityItemRes
                        .builder()
                        .number(number)
                        .quantity(0)
                        .build();
                numberQuantityItemResList.add(numberQuantityItemRes);
            }
            QuantityValuesByDateAndNumbersItemRes itemRes = QuantityValuesByDateAndNumbersItemRes
                    .builder()
                    .date(item.getDate())
                    .values(item.getValue())
                    .numberQuantityItemRes(numberQuantityItemResList)
                    .build();
            resultIem.add(itemRes);
        }

        for (QuantityValuesByDateAndNumbersItemRes item : resultIem) {
            if (item.getValues() != null) {
                List<String> values = Arrays.asList(item.getValues().split(","));
                for (NumberQuantityItemRes numberQuantityItemRes : item.getNumberQuantityItemRes()) {
                    List<String> numbers = Arrays.asList(numberQuantityItemRes.getNumber().split("-"));
                    for (String number : numbers) {
                        for (String value : values) {
                            if (req.getHead() != 1 ?
                                    value.substring(value.length() - 2).equals(number)
                                    : value.substring(0, 2).equals(number)) {
                                numberQuantityItemRes.setQuantity(numberQuantityItemRes.getQuantity() + 1);
                            }
                        }
                    }
                }
            }
        }
        result.setData(resultIem);
        return new BaseResponse().success(result);
    }

    @Override
    public BaseResponse statistic(StatisticMultiValuesReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        if (req.getValues().isEmpty()) {
            return new BaseResponse().fail("Danh sách thống kê không được trống!");
        }
        return new BaseResponse().success(find(req));
    }

    @Override
    public BaseResponse getTodayNumbersStatistics(TodayNumberStatisticsReq req) {
        if (req.getQuantity() != 2 && req.getQuantity() != 3) {
            return new BaseResponse().fail("quantity chỉ là 2 va 3");
        }
        int check = 0;
        if (req.getOperatorType() == 0) {
            if (req.getQuantity() == 2) {
                check = 13;
            }

            if (req.getQuantity() == 3) {
                check = 10;
            }
        } else {
            if (req.getQuantity() == 2) {
                check = 165;
            }

            if (req.getQuantity() == 3) {
                check = 400;
            }
        }
        List<Integer> checkList = new ArrayList<>();
        List<Integer> findList = new ArrayList<>();
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0")
                .page(0)
                .size(30000)
                .sort("date,desc")
                .build();
        List<DateMultiValues> tempDateValues = this.search(searchReq).getContent();
        StatisticMultiValuesRes result = new StatisticMultiValuesRes();
        do {
            for (int i = 0; i < tempDateValues.size(); i++) {
                if (i < check){
                    checkList.addAll(ListUtil.getLastTwoCharsAsIntegers(Arrays.asList(tempDateValues.get(i).getValue().split(","))));
                }else {
                    List<Integer> data = ListUtil.getLastTwoCharsAsIntegers(Arrays.asList(tempDateValues.get(i).getValue().split(",")));
                    data.removeAll(checkList);
                    if (!data.isEmpty()){
                        if (data.size() >= check){
                            for (int j = 0; j < check; j++){
                                findList.add(data.get(j));
                            }
                        }else {
                            checkList.addAll(data);
                            findList.addAll(generateUniqueRandomNumbers(req.getQuantity() - data.size(), checkList));
                            findList.addAll(data);
                        }
                        StatisticMultiValuesRes resultTemp = find(StatisticMultiValuesReq.builder().startDate(20100101L).endDate(DateUtil.getCurrenDate()).values(findList).build());
                        if (resultTemp.getMaxGap() - resultTemp.getStubbornnessLevel() < 5 && resultTemp.getMaxGap() - resultTemp.getStubbornnessLevel() >= 1){
                            result = resultTemp;
                            break;
                        }else {
                            findList.clear();
                        }
                    }
                }
            }
            check ++;
        }while (result.getData() == null);

//        StatisticMultiValuesRes result = find(StatisticMultiValuesReq.builder().startDate(20100101L).endDate(DateUtil.getCurrenDate()).values(findList).build());
        return new BaseResponse().success(result);
    }

    public StatisticMultiValuesRes find(StatisticMultiValuesReq req) {
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(300000)
                .sort("date,asc")
                .build();

        List<DateMultiValues> data = this.search(searchReq).getContent();

        int countGap = 1;
        int maxGap = 0;
        Long maxEndDate = 0L;
        int check = 0;
        Long lastDate = 0L;
        Long duringDate = 0L;
        int checkNull = 0;
        if (req.getConcurOccur() == 0) {
            for (DateMultiValues element : data) {
                if (element.getValue() != null) {
                    List<Integer> values = ListUtil.getLastTwoCharsAsIntegers(Arrays.asList(element.getValue().split(",")));
                    if (!ListUtil.hasCommonElement(req.getValues(), values)) {
                        countGap++;
                        if (countGap > maxGap) {
                            maxGap = countGap;
                            maxEndDate = element.getDate();
                        }
                    } else {
                        countGap = 1;
                        check = 1;
                        duringDate = element.getDate();
                    }
                }
            }
        } else {
            for (DateMultiValues element : data) {
                if (element.getValue() != null) {
                    List<Integer> values = ListUtil.getLastTwoCharsAsIntegers(Arrays.asList(element.getValue().split(",")));
                    if (!values.containsAll(req.getValues())) {
                        countGap++;
                        if (countGap > maxGap) {
                            maxGap = countGap;
                            maxEndDate = element.getDate();
                        }
                    } else {
                        countGap = 1;
                        check = 1;
                        duringDate = element.getDate();
                    }
                }
            }
        }
        maxEndDate = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxEndDate : DateUtil.sum(maxEndDate, 1);
        maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
        Long maxStartDate = DateUtil.subtract(maxEndDate, maxGap);


        SearchReq searchReqCheck = SearchReq.builder()
                .filter("id>0")
                .page(0)
                .size(30000)
                .sort("date,desc")
                .build();
        List<DateMultiValues> dateValuesCheck = this.search(searchReqCheck).getContent();
        for (DateMultiValues e : dateValuesCheck) {
            if (e.getDate().equals(maxStartDate)) {
                if (e.getValue() == null) {
                    maxStartDate = DateUtil.subtract(maxStartDate, 1);
                }
            }
        }
        for (DateMultiValues e : dateValuesCheck) {
            if (e.getDate() >= maxStartDate && e.getDate() <= maxEndDate) {
                if (e.getValue() == null) {
                    checkNull++;
                }
            }
        }
        maxStartDate = DateUtil.subtract(maxStartDate, checkNull);
        if (req.getConcurOccur() == 0) {
            for (DateMultiValues element : dateValuesCheck) {
                if (element.getValue() != null) {
                    List<Integer> values = ListUtil.getLastTwoCharsAsIntegers(Arrays.asList(element.getValue().split(",")));
                    if (ListUtil.hasCommonElement(req.getValues(), values)) {
                        lastDate = element.getDate();
                        break;
                    }
                }
            }
        } else {
            for (DateMultiValues element : dateValuesCheck) {
                if (element.getValue() != null) {
                    List<Integer> values = ListUtil.getLastTwoCharsAsIntegers(Arrays.asList(element.getValue().split(",")));
                    if (values.containsAll(req.getValues())) {
                        lastDate = element.getDate();
                        break;
                    }
                }
            }
        }

        StatisticMultiValuesRes res;
        if (check == 1) {
            res = StatisticMultiValuesRes.builder()
                    .data(req.getValues())
                    .startDate(req.getStartDate())
                    .endDate(req.getEndDate())
                    .maxStartDate(maxStartDate)
                    .maxEndDate(maxEndDate)
                    .lastDate(lastDate)
                    .maxGap(maxGap)
                    .duringDate(duringDate)
                    .stubbornnessLevel(DateUtil.calculateDaysBetween(lastDate, DateUtil.getCurrenDate()) - 1)
                    .check(check)
                    .build();
        } else {
            res = StatisticMultiValuesRes.builder()
                    .stubbornnessLevel(DateUtil.calculateDaysBetween(lastDate, DateUtil.getCurrenDate()) - 1)
                    .lastDate(lastDate)
                    .check(check)
                    .build();
        }
        return res;
    }

    public List<String> getDataDate() {
        // Định dạng ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Ngày bắt đầu và ngày kết thúc
        LocalDate startDate = LocalDate.of(2024, 12, 16);
        LocalDate endDate = LocalDate.of(2024, 12, 19);

        // Tạo một danh sách để chứa các ngày
        List<String> dateList = new ArrayList<>();

        // Vòng lặp qua tất cả các ngày từ startDate đến endDate
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // Thêm ngày hiện tại vào danh sách
            dateList.add(currentDate.format(formatter));
            // Tiến đến ngày tiếp theo
            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }

        return dateList;
    }

    public static List<Integer> generateUniqueRandomNumbers(int n, List<Integer> existingList) {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();


        // Generate unique random numbers until we reach the desired count
        while (uniqueNumbers.size() < n) {
            int randomNumber = random.nextInt(100); // Random number between 0 and 99

            // Check if the random number is not in the existing list
            if (!existingList.contains(randomNumber)) {
                uniqueNumbers.add(randomNumber);
            }
        }

        return new ArrayList<>(uniqueNumbers);
    }
}
